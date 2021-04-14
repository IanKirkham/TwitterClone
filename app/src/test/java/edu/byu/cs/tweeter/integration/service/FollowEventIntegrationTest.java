package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowEventServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowEventIntegrationTest {
    private static FollowEventServiceProxy proxy;
    private static FollowUserRequest followUserRequest;
    private static UnfollowUserRequest unfollowUserRequest;
    private static String user1;
    private static AuthToken authToken;
    private static String user2;

    private static FollowUserResponse followUserResponse;
    private static UnfollowUserResponse unfollowUserResponse;

    @BeforeAll
    static void setup() {
        proxy = new FollowEventServiceProxy();
        user1 = "@TestUser";
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");
        user2 = "@BonnieBeatty";

        followUserRequest = new FollowUserRequest(user1, authToken, user2, user1, user2);
        unfollowUserRequest = new UnfollowUserRequest(user1, authToken, user2, user1, user2);

        followUserResponse = new FollowUserResponse(true, "Successfully followed user");
        unfollowUserResponse = new UnfollowUserResponse(true, "Successfully unfollowed user");

    }

    @Test
    void followUserTest() throws IOException, TweeterRemoteException {
        FollowUserResponse response = proxy.followUser(followUserRequest);
        assertEquals(followUserResponse, response);
    }

    @Test
    void unfollowUserTest() throws IOException, TweeterRemoteException {
        UnfollowUserResponse response = proxy.unfollowUser(unfollowUserRequest);
        assertEquals(unfollowUserResponse, response);
    }
}
