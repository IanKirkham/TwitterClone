package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * A DAO for accessing 'follows' data from the database.
 */
public class FollowsDAO {
    private static final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    private static final Table table = dynamoDB.getTable("follows");

    // TODO: implement the below code for get Followers (Template / Strategy Pattern?)
//    public UserResponse getFollowers(FollowerRequest request) {
//
//    }

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

//    public UserResponse getFollowees(FolloweeRequest request) {
//
//    }
}
