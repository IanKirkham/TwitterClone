package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostIntegrationTest {
    private static PostServiceProxy proxy;
    private static Status status;
    private static PostRequest validRequest;
    private static PostResponse successResponse;

    private static final LocalDateTime time1 = LocalDateTime.now();

    /**
     * Create a PostService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeAll
    static void setup() throws IOException, TweeterRemoteException {
        proxy = new PostServiceProxy();
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        String content1 = "Content: Hello World!, Mentions: @BobBobson, URLs: http://www.google.com";
        status = new Status(content1, currentUser, time1);
        validRequest = new PostRequest(currentUser, content1, time1);

        successResponse = new PostResponse(status);
    }

    @Test
    void postStatusTest() throws IOException, TweeterRemoteException {
        PostResponse response = proxy.savePost(validRequest);
        assertEquals(successResponse, response);
    }
}
