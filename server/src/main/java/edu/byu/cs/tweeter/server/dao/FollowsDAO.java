package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

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
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import jdk.nashorn.internal.runtime.ECMAException;

/**
 * A DAO for accessing 'follows' data from the database.
 */
public class FollowsDAO {
    private static final String R_HANDLE = "follower_handle";
    private static final String E_HANDLE = "followee_handle";
    private static final String R_NAME   = "follower_name";
    private static final String E_NAME   = "followee_name";

    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable("follows");

    public UserResponse getFollowers(FollowerRequest request) {
        // TODO: Authenticate

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
            items = table.getIndex("follows_index").query(querySpec);
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

    // TODO: Template/Strategy pattern?
    public UserResponse getFollowees(FolloweeRequest request) {
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

    public Integer getFolloweeCount(User follower) {
        if (follower == null) {
            throw new RuntimeException("[Bad Request] Invalid user");
        }
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("follower_handle = :v_name")
                .withValueMap(new ValueMap()
                        .withString(":v_name", follower.getAlias()));

        return table.query(querySpec).getAccumulatedItemCount(); // TODO: I am not sure if this is the right way to get the count, there may be a better way
    }

    public DoesFollowResponse doesFollowUser(DoesFollowRequest request) {
        // TODO: Authenticate
        if (get(request.getRootUser().getAlias(), request.getCurrentUser().getAlias()) != null) {
            return new DoesFollowResponse(true);
        } else {
            return new DoesFollowResponse(false);
        }
    }

    public FollowUserResponse followUser(FollowUserRequest request) {
        // TODO: Authenticate

        FollowRelationship followRelationship = new FollowRelationship(request.getRootUser().getAlias(),
                                                                        request.getRootUser().getName(),
                                                                        request.getCurrentUser().getAlias(),
                                                                        request.getCurrentUser().getName());
        if (put(followRelationship)) {
            return new FollowUserResponse(true, "Successfully followed user");
        } else {
            return new FollowUserResponse(false, "[Internal Error] Failed to follow user");
        }
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        // TODO: Authenticate

        FollowRelationship followRelationship = new FollowRelationship(request.getRootUser().getAlias(),
                request.getRootUser().getName(),
                request.getCurrentUser().getAlias(),
                request.getCurrentUser().getName());
        if (delete(followRelationship)) {
            return new UnfollowUserResponse(true, "Successfully unfollowed user");
        } else {
            return new UnfollowUserResponse(false, "[Internal Error] Failed to unfollow user");
        }
    }

    public List<String> getFollowers(String userAlias) {
        // TODO: Authenticate

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
            items = table.getIndex("follows_index").query(querySpec);
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
}
