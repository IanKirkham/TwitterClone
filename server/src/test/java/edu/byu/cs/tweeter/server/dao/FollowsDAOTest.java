package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
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

class FollowsDAOTest {
    private static FollowsDAO dao;
    private static AuthToken authToken;
    private static String userAlias1 = "@AllenAnderson";;
    private static String userAlias2 = "@nonexistent";

    @BeforeAll
    static void setup() {
        dao = new FollowsDAO();
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");
    }

    @Test
    void testGetFollowers_noFollowersForUser() {
        FollowerRequest request = new FollowerRequest(userAlias2, 10, null, authToken);
        UserResponse response = dao.getFollowers(request);

        Assertions.assertNull(response.getUsers());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_limitGreaterThanUsers() {
        FollowerRequest request = new FollowerRequest(userAlias1, 25, null, authToken);
        UserResponse response = dao.getFollowers(request);

        Assertions.assertTrue(response.getUsers().size() > 0);
        Assertions.assertTrue(response.getUsers().size() < 25);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowers_limitLessThanUsers() {
        FollowerRequest request = new FollowerRequest(userAlias1, 2, null, authToken);
        UserResponse response = dao.getFollowers(request);

        // Verify first page
        Assertions.assertEquals(2, response.getUsers().size());
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowerRequest(userAlias1, 2, response.getLastKey(), authToken);
        response =  dao.getFollowers(request);

        Assertions.assertEquals(2, response.getUsers().size());
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_noFolloweesForUser() {
        FolloweeRequest request = new FolloweeRequest(userAlias2, 10, null, authToken);
        UserResponse response = dao.getFollowees(request);

        Assertions.assertNull(response.getUsers());
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitGreaterThanUsers() {
        FolloweeRequest request = new FolloweeRequest(userAlias1, 25, null, authToken);
        UserResponse response = dao.getFollowees(request);

        Assertions.assertTrue(response.getUsers().size() > 0);
        Assertions.assertTrue(response.getUsers().size() < 25);
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitLessThanUsers() {
        FolloweeRequest request = new FolloweeRequest(userAlias1, 2, null, authToken);
        UserResponse response = dao.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getUsers().size());
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FolloweeRequest(userAlias1, 2, response.getLastKey(), authToken);
        response =  dao.getFollowees(request);

        Assertions.assertEquals(2, response.getUsers().size());
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    void testGetFollowersCount_AndDoesFollow_AndFollowUser() {
        GetCountRequest request = new GetCountRequest(userAlias1);
        GetCountResponse response = dao.getFollowerCount(request);
        int count = response.getCount();

        FollowUserRequest followUserRequest = new FollowUserRequest(userAlias2, authToken, userAlias1, userAlias2, userAlias1);
        FollowUserResponse followUserResponse = dao.followUser(followUserRequest);

        Assertions.assertTrue(followUserResponse.isSuccess());

        response = dao.getFollowerCount(request);
        Assertions.assertEquals(count + 1, response.getCount());

        DoesFollowRequest doesFollowRequest = new DoesFollowRequest(userAlias2, authToken, userAlias1, userAlias2, userAlias1);
        DoesFollowResponse doesFollowResponse = dao.doesFollowUser(doesFollowRequest);
        Assertions.assertTrue(doesFollowResponse.isSuccess());

        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(userAlias2, authToken, userAlias1, userAlias2, userAlias1);
        UnfollowUserResponse unfollowUserResponse = dao.unfollowUser(unfollowUserRequest);

        Assertions.assertTrue(unfollowUserResponse.isSuccess());

        response = dao.getFollowerCount(request);
        Assertions.assertEquals(count, response.getCount());

        doesFollowResponse = dao.doesFollowUser(doesFollowRequest);
        Assertions.assertFalse(doesFollowResponse.isSuccess());
    }


    @Test
    void testGetFolloweesCount_AndDoesFollow_AndFollowUser() {
        GetCountRequest request = new GetCountRequest(userAlias1);
        GetCountResponse response = dao.getFolloweeCount(request);
        int count = response.getCount();

        FollowUserRequest followUserRequest = new FollowUserRequest(userAlias1, authToken, userAlias2, userAlias1, userAlias2);
        FollowUserResponse followUserResponse = dao.followUser(followUserRequest);

        Assertions.assertTrue(followUserResponse.isSuccess());

        response = dao.getFolloweeCount(request);
        Assertions.assertEquals(count + 1, response.getCount());

        DoesFollowRequest doesFollowRequest = new DoesFollowRequest(userAlias1, authToken, userAlias2, userAlias1, userAlias2);
        DoesFollowResponse doesFollowResponse = dao.doesFollowUser(doesFollowRequest);
        Assertions.assertTrue(doesFollowResponse.isSuccess());

        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(userAlias1, authToken, userAlias2, userAlias1, userAlias2);
        UnfollowUserResponse unfollowUserResponse = dao.unfollowUser(unfollowUserRequest);

        Assertions.assertTrue(unfollowUserResponse.isSuccess());

        response = dao.getFolloweeCount(request);
        Assertions.assertEquals(count, response.getCount());

        doesFollowResponse = dao.doesFollowUser(doesFollowRequest);
        Assertions.assertFalse(doesFollowResponse.isSuccess());
    }

}