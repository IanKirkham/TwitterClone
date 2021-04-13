package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * A DAO for accessing 'feed' data from the database.
 */
public class FeedDAO {

    // Table
    private static final String TableName = "feed";

    // Attributes
    private static final String AliasAttr = "alias";
    private static final String TimePublishedAttr = "timePublished";
    private static final String AuthorAttr = "author";
    private static final String ContentAttr = "content";

    // DynamoDB Client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);

    public StatusesResponse getFeed(FeedRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getUserAlias(), request.getAuthToken())) {
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
                User user = userDAO.getUser(item.get(AuthorAttr).getS());
                Status status = new Status(item.get(ContentAttr).getS(),  user, LocalDateTime.parse(item.get(TimePublishedAttr).getS()));
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

    public void addPostBatch(List<String> aliases, String author, String timePublished, String content) {
        TableWriteItems items = new TableWriteItems(TableName);

        for (String alias : aliases) {
            Item item = new Item()
                    .withPrimaryKey(AliasAttr, alias)
                    .withString(AuthorAttr, author)
                    .withString(TimePublishedAttr, timePublished)
                    .withString(ContentAttr, content);
            items.addItemToPut(item);
        }
        loopBatchWrite(items);
    }

    private void loopBatchWrite(TableWriteItems items) {
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        // If items failed to save, try again.
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }
}
