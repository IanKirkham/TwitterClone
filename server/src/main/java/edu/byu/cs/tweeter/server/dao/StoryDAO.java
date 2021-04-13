package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * A DAO for accessing 'story' data from the database.
 */
public class StoryDAO {

    // Table
    private static final String TableName = "story";

    // Attributes
    private static final String AliasAttr = "alias";
    private static final String TimePublishedAttr = "timePublished";
    private static final String ContentAttr = "content";

    // DynamoDB Client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable(TableName);

    public StatusesResponse getStory(StoryRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getAuthToken().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Invalid limit");
        }
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] User not found");
        }

        List<Status> statuses = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", AliasAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getUserAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TableName)
                .withKeyConditionExpression("#alias = :alias")
                .withScanIndexForward(false)
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if (request.getLastStatusKey() != null) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AliasAttr, new AttributeValue().withS(request.getUserAlias()));
            startKey.put(TimePublishedAttr, new AttributeValue().withS(request.getLastStatusKey()));
            queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = client.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            UserDAO userDAO = new UserDAO();

            for (Map<String, AttributeValue> item : items) {
                User user = userDAO.getUser(item.get(AliasAttr).getS());
                Status status = new Status(item.get(ContentAttr).getS(), user, LocalDateTime.parse(item.get(TimePublishedAttr).getS()));
                statuses.add(status);
            }
        }

        String lastKeyString = null;
        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            lastKeyString = lastKey.get(TimePublishedAttr).getS();
        }

        return new StatusesResponse(statuses, lastKeyString != null, lastKeyString);
    }

    public PostResponse savePost(PostRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getAuthor(), request.getAuthToken())) {
            AuthDAO.invalidateToken(request.getAuthToken());
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        Item item = new Item()
                .withPrimaryKey(AliasAttr, request.getAuthor())
                .withString(TimePublishedAttr, request.getTimePublished().toString())
                .withString(ContentAttr, request.getContent());

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(request.getAuthor());

        Status status = new Status(request.getContent(), user, request.getTimePublished());
        try {
            table.putItem(item);
            return new PostResponse(status);
        } catch (Exception e) {
            throw new RuntimeException("[Internal Error] Failed to insert Status into Story");
        }
    }
}
