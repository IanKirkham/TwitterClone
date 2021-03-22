package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.service.StatusesServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusesIntegrationTest {
    private static StatusesServiceProxy proxy;
    private static StatusesRequest validFeedRequest;
    private static StatusesRequest validStoryRequest;
    private static User user1;
    private static AuthToken authToken;
    private static StatusesResponse successFeedResponse;
    private static StatusesResponse successStoryResponse;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeAll
    static void setup() {
        proxy = new StatusesServiceProxy();

        User currentUser = new User("Test", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user1 = new User("Allen", "Anderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";
        String content2 = "Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);
        Status status2 = new Status(content2, currentUser, time1.plus(Duration.ofSeconds(1)));
        Status status3 = new Status(content1, user1, time1);
        Status status4 = new Status(content2, user1, time1.plus(Duration.ofSeconds(1)));

        currentUser.setFollowees(new ArrayList<>(Arrays.asList(user1.getAlias())));

        // Setup request objects to use in the tests
        validFeedRequest = new StatusesRequest(currentUser.getFollowees(), 2, null);
        validStoryRequest = new StatusesRequest(new ArrayList<>(Arrays.asList(currentUser.getAlias())), 2, null);

        successFeedResponse = new StatusesResponse(Arrays.asList(status3, status4), false);
        successStoryResponse = new StatusesResponse(Arrays.asList(status1, status2), false);
    }

    @Test
    void statusesFeedTest() throws IOException, TweeterRemoteException {
        StatusesResponse statusesResponse = proxy.getStatuses(validFeedRequest);
        for (int i = 0; i < successFeedResponse.getStatuses().size(); i++) {
            statusesResponse.getStatuses().get(i).setTimePublished(successFeedResponse.getStatuses().get(i).getTimePublished());
        }
        assertEquals(successFeedResponse, statusesResponse);
    }

    @Test
    void statusesStoryTest() throws IOException, TweeterRemoteException {
        StatusesResponse statusesResponse = proxy.getStatuses(validStoryRequest);
        for (int i = 0; i < successStoryResponse.getStatuses().size(); i++) {
            statusesResponse.getStatuses().get(i).setTimePublished(successStoryResponse.getStatuses().get(i).getTimePublished());
        }
        assertEquals(successStoryResponse, statusesResponse);
    }
}
