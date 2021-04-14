package edu.byu.cs.tweeter.integration.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.client.model.service.StatusesServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatusesIntegrationTest {
    private static StatusesServiceProxy proxy;
    private static FeedRequest validFeedRequest;
    private static StoryRequest validStoryRequest;
    private static AuthToken authToken;

    private static final LocalDateTime time1 = LocalDateTime.now();

    @BeforeAll
    static void setup() {
        proxy = new StatusesServiceProxy();
        authToken = new AuthToken("@TestUser", "p_S7ylnLZ6bV9-feB55GUPKt99Xu94Ia");

        // Setup request objects to use in the tests
        validFeedRequest = new FeedRequest("@TestUser", 10, null, authToken);
        validStoryRequest = new StoryRequest("@TestUser", 10, null, authToken);
    }

    @Test
    void statusesFeedTest() throws IOException, TweeterRemoteException {
        StatusesResponse statusesResponse = proxy.getFeed(validFeedRequest);
        assertTrue(statusesResponse.isSuccess());
        assertNotNull(statusesResponse.getStatuses());
    }

    @Test
    void statusesStoryTest() throws IOException, TweeterRemoteException {
        StatusesResponse statusesResponse = proxy.getStory(validStoryRequest);
        assertTrue(statusesResponse.isSuccess());
        assertNotNull(statusesResponse.getStatuses());
    }
}
