package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserIntegrationTest {
    private static UserServiceProxy proxy;
    private static FollowerRequest followerRequest;
    private static FolloweeRequest followeeRequest;
    private static AuthToken authToken;

    @BeforeAll
    static void setup() {
        proxy = new UserServiceProxy();

        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");

        followerRequest = new FollowerRequest("@TestUser", 10, null, authToken);
        followeeRequest = new FolloweeRequest("@TestUser", 10, null, authToken);
    }


    @Test
    void getFollowersTest() throws IOException, TweeterRemoteException {
        UserResponse userResponse = proxy.getFollowers(followerRequest);
        assertEquals("@TestUser", userResponse.getQueriedUser().getAlias());
        assertNotNull(userResponse.getUsers());
    }

    @Test
    void getFolloweesTest() throws IOException, TweeterRemoteException {
        UserResponse userResponse = proxy.getFollowees(followeeRequest);
        assertEquals("@TestUser", userResponse.getQueriedUser().getAlias());
        assertNotNull(userResponse.getUsers());
    }
}
