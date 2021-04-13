package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.FollowRelationship;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * A DAO for accessing 'follows' data from the database.
 */
public class FollowsDAO {

    // Table and Index
    private static final String TableName = "follows";
    private static final String IndexName = "follows_index";

    // Attributes
    private static final String R_HANDLE = "follower_handle";
    private static final String E_HANDLE = "followee_handle";
    private static final String R_NAME   = "follower_name";
    private static final String E_NAME   = "followee_name";

    // DynamoDB client
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable(TableName);

    public UserResponse getFollowers(FollowerRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getAuthToken().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", E_HANDLE);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":e", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#handle = :e").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true).withMaxPageSize(request.getLimit()).withMaxResultSize(request.getLimit());
        if (request.getLastUserAlias() != null) {
            querySpec.withExclusiveStartKey(R_HANDLE, request.getLastUserAlias(), E_HANDLE, request.getUserAlias());
        }

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> handles = new ArrayList<>();

        try {
            items = table.getIndex(IndexName).query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                handles.add(item.getString(R_HANDLE));
            }

            Map<String, AttributeValue> last = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
            String lastUserAlias = null;
            if (last != null && last.get(R_HANDLE) != null) {
                lastUserAlias = last.get(R_HANDLE).getS();
            }

            UserDAO userDAO = new UserDAO();
            User queriedUser = userDAO.getUser(request.getUserAlias());
            if (queriedUser == null) {
                return new UserResponse("[Bad Request] Queried User does not exist", "");
            }

            List<User> users = new ArrayList<>();
            for (String alias : handles) {
                User user = userDAO.getUser(alias);
                if (user == null) {
                    continue;
                }
                users.add(user);
            }

            return new UserResponse(queriedUser, users, lastUserAlias != null, lastUserAlias);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserResponse getFollowees(FolloweeRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getAuthToken().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", R_HANDLE);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":r", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#handle = :r").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true).withMaxPageSize(request.getLimit()).withMaxResultSize(request.getLimit());
        if (request.getLastUserAlias() != null) {
            querySpec.withExclusiveStartKey(R_HANDLE, request.getUserAlias(), E_HANDLE, request.getLastUserAlias());
        }

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> handles = new ArrayList<>();

        try {
            items = table.query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                handles.add(item.getString(E_HANDLE));
            }

            Map<String, AttributeValue> last = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();

            String lastUserAlias = null;
            if (last != null && last.get(E_HANDLE) != null) {
                lastUserAlias = last.get(E_HANDLE).getS();
            }

            UserDAO userDAO = new UserDAO();
            User queriedUser = userDAO.getUser(request.getUserAlias());
            if (queriedUser == null) {
                return new UserResponse("[Bad Request] Queried User does not exist", "");
            }

            List<User> users = new ArrayList<>();
            for (String alias : handles) {
                User user = userDAO.getUser(alias);
                if (user == null) {
                    continue;
                }
                users.add(user);
            }

            return new UserResponse(queriedUser, users, lastUserAlias != null, lastUserAlias);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // If this is really costly, we should just store the follower/followee count on the user object.
    // B/c currently, I think this query has to scan the whole table. I don't think would scale well.
    public GetCountResponse getFolloweeCount(GetCountRequest request) {
        Condition followerCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(request.getAlias()));
        Map<String, Condition> keyConditions = new HashMap<>();
        keyConditions.put(R_HANDLE, followerCondition);

        QueryRequest queryRequest = new QueryRequest(TableName);
        queryRequest.setSelect(Select.COUNT);
        queryRequest.setKeyConditions(keyConditions);

        QueryResult result = client.query(queryRequest);
        Integer count = result.getCount();

        return new GetCountResponse(count);
    }

    // If this is really costly, we should just store the follower/followee count on the user object.
    // B/c currently, I think this query has to scan the whole table. I don't think would scale well.
    public GetCountResponse getFollowerCount(GetCountRequest request) {
        Condition followerCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(request.getAlias()));
        Map<String, Condition> keyConditions = new HashMap<>();
        keyConditions.put(E_HANDLE, followerCondition);

        QueryRequest queryRequest = new QueryRequest(TableName);
        queryRequest.setIndexName(IndexName);
        queryRequest.setSelect(Select.COUNT);
        queryRequest.setKeyConditions(keyConditions);

        QueryResult result = client.query(queryRequest);
        Integer count = result.getCount();

        return new GetCountResponse(count);
    }

    public DoesFollowResponse doesFollowUser(DoesFollowRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getAuthToken().getAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        if (get(request.getPrimaryUserAlias(), request.getCurrentUserAlias()) != null) {
            return new DoesFollowResponse(true);
        } else {
            return new DoesFollowResponse(false);
        }
    }

    public FollowUserResponse followUser(FollowUserRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getPrimaryUserAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        FollowRelationship followRelationship = new FollowRelationship(request.getPrimaryUserAlias(),
                                                                        request.getPrimaryUserName(),
                                                                        request.getCurrentUserAlias(),
                                                                        request.getCurrentUserName());
        if (put(followRelationship)) {
            return new FollowUserResponse(true, "Successfully followed user");
        } else {
            return new FollowUserResponse(false, "[Internal Error] Failed to follow user");
        }
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        // Authenticate
        if (!AuthDAO.isValidTokenForUser(request.getPrimaryUserAlias(), request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Authentication Failed");
        }

        FollowRelationship followRelationship = new FollowRelationship(request.getPrimaryUserAlias(),
                request.getPrimaryUserName(),
                request.getCurrentUserAlias(),
                request.getCurrentUserName());
        if (delete(followRelationship)) {
            return new UnfollowUserResponse(true, "Successfully unfollowed user");
        } else {
            return new UnfollowUserResponse(false, "[Internal Error] Failed to unfollow user");
        }
    }

    public List<String> getFollowers(String userAlias) {
        // This method is only accessible on the server side and is only used by the SQS handler

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", E_HANDLE);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":e", userAlias);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#handle = :e").withNameMap(nameMap)
                .withValueMap(valueMap).withScanIndexForward(true);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        List<String> handles = new ArrayList<>();

        try {
            items = table.getIndex(IndexName).query(querySpec);
            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                handles.add(item.getString(R_HANDLE));
            }
            return handles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean put(FollowRelationship followRelationship) {
        try {
            String r_handle = followRelationship.getFollower_handle();
            String e_handle = followRelationship.getFollowee_handle();
            String r_name = followRelationship.getFollower_name();
            String e_name = followRelationship.getFollowee_name();
            PutItemOutcome outcome = table.putItem(new Item()
                            .withPrimaryKey(R_HANDLE, r_handle, E_HANDLE, e_handle)
                            .withString(R_NAME, r_name).withString(E_NAME, e_name));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private FollowRelationship get(String follower, String followee) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(R_HANDLE, follower, E_HANDLE, followee);

        try {
            Item outcome = table.getItem(spec);
            if (outcome == null) {
                return null;
            }
            return new FollowRelationship(outcome.getString(R_HANDLE), outcome.getString(R_NAME), outcome.getString(E_HANDLE), outcome.getString(E_NAME));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean delete(FollowRelationship followRelationship) {
        String follower = followRelationship.getFollower_handle();
        String followee = followRelationship.getFollowee_handle();
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey(R_HANDLE, follower, E_HANDLE, followee));

        try {
            table.deleteItem(deleteItemSpec);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addFollowersBatch(List<String> aliases) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TableName);

        // Add each user into the TableWriteItems object
        for (String alias : aliases) {
            Item item = null;
            try {
                item = new Item()
                        .withPrimaryKey(R_HANDLE, alias)
                        .withString(E_HANDLE, "@TestUser")
                        .withString(R_NAME, alias)
                        .withString(E_NAME, "Test User");
            } catch (Exception e) {
                e.printStackTrace();
            }
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(TableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }
}
