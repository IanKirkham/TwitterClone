package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostIntegrationTest {
    private static PostServiceProxy proxy;
    private static Status status;
    private static PostRequest validRequest;
    private static AuthToken authToken;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeAll
    static void setup() throws IOException, TweeterRemoteException {
        proxy = new PostServiceProxy();
        User currentUser = new User("Delete", "Me", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        String content1 = "Content: Hello World!, Mentions: @BobBobson, URLs: http://www.google.com";
        status = new Status(content1, currentUser, time1);
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");
        validRequest = new PostRequest(currentUser.getAlias(), content1, time1, authToken);
    }

    @Test
    void postStatusTest() throws IOException, TweeterRemoteException {
        PostResponse response = proxy.savePost(validRequest);
        assertNotNull(response.getStatus());
    }
}
