package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
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
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StoryDAO {

    // Table
    private static final String TableName = "story";

    // Attributes
    private static final String AliasAttr = "alias";
    private static final String TimePublishedAttr = "timePublished";
    private static final String ContentAttr = "content";

    // DynamoDB Client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    //private static final DynamoDB dynamoDB = new DynamoDB(client);

    public StatusesResponse getStory(StoryRequest request) {
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
            for (Map<String, AttributeValue> item : items) {
                Status status = new Status(item.get(ContentAttr).getS(), new User(), LocalDateTime.parse(item.get(TimePublishedAttr).getS())); // TODO: change user.. do we need to do a database lookup?
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

//    private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {
//        int statusesIndex = 0;
//
//        if (lastStatus != null) {
//            for (int i = 0; i < allStatuses.size(); i++) {
//                if (lastStatus.equals(allStatuses.get(i))) {
//                    statusesIndex = i + 1;
//                    break;
//                }
//            }
//        }
//
//        return statusesIndex;
//    }

//    List<Status> getStatuses(Table table, StoryRequest request) {
//        List<Status> statuses = new ArrayList<>();
//
//        Map<String, AttributeValue> exclusiveStartKey = null;
//        if (request.getLastStatus() != null) {
//            exclusiveStartKey = new HashMap<String, AttributeValue>();
//            exclusiveStartKey.put("alias", new AttributeValue().withS(request.getLastStatus().getAuthor()));
//            exclusiveStartKey.put("timePublished", new AttributeValue().withS(request.getLastStatus().getTimePublished().toString()));
//        }
//
//        QuerySpec querySpec = new QuerySpec()
//                .withScanIndexForward(false) // sort descending?
//                .withKeyConditionExpression("alias = :v_alias")
//                .withExclusiveStartKey((KeyAttribute) exclusiveStartKey)
//                .withValueMap(new ValueMap()
//                    .withString(":v_alias", request.getUserAlias()));
//
//        ItemCollection<QueryOutcome> items = null;
//        Iterator<Item> iterator = null;
//        Item item = null;
//
//        try {
//            items = table.query(querySpec);
//
//            iterator = items.iterator();
//            while (iterator.hasNext()) {
//                item = iterator.next();
//                statuses.add(new Status(item.get("content").toString(), item.get("alias").toString(), item.get("timePublished")));
//            }
//
//        }
//        catch (Exception e) {
//            System.err.println("Unable to query data");
//            System.err.println(e.getMessage());
//        }
//    }
//
//    List<Status> getDummyStatuses(List<String> userAliases) {
//        List<Status> statuses = new ArrayList<>(Arrays.asList(status1, status2, status3, status4, status5, status6,
//                status7, status8, status9, status10, status11, status12, status13, status14, status15, status16));
//        statuses.addAll(sessionStatuses);
//        statuses.removeIf(status -> !userAliases.contains(status.getAuthor().getAlias())); // inefficient, but this is dummy code. The database will replace this.
//        statuses.sort((s1, s2) -> s1.getTimePublished().compareTo(s2.getTimePublished()));
//        return statuses;
//    }
//
//    public PostResponse savePost(PostRequest request) {
//        Status status = new Status(request.getContent(), request.getAuthor(), request.getTimePublished());
//        return new PostResponse(status);
//    }
}
