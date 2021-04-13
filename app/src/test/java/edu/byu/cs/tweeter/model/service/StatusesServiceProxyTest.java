package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.service.StatusesServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StatusesServiceProxyTest {

    private FeedRequest validFeedRequest;
    private StoryRequest validStoryRequest;

    private FeedRequest invalidFeedRequest;
    private StoryRequest invalidStoryRequest;

    private StatusesResponse successFeedResponse;
    private StatusesResponse successStoryResponse;

    private StatusesResponse failureFeedResponse;
    private StatusesResponse failureStoryResponse;

    private StatusesServiceProxy statusesServiceProxySpy;

    private static final LocalDateTime time1 = LocalDateTime.now();
    private static final int PAGE_SIZE = 10;

    /**
     * Create a StatusesService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user1 = new User("Allen", "Anderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";
        String content2 = "Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);
        Status status2 = new Status(content2, currentUser, time1.plus(Duration.ofSeconds(1)));
        Status status3 = new Status(content1, user1, time1);
        Status status4 = new Status(content2, user1, time1.plus(Duration.ofSeconds(1)));

        // Setup request objects to use in the tests
        validFeedRequest = new FeedRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        validStoryRequest = new StoryRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());

        invalidFeedRequest = new FeedRequest(null, 0, null, null);
        invalidStoryRequest = new StoryRequest(null, 0, null, null);

        // Setup response objects to use in the tests
        successFeedResponse = new StatusesResponse(Arrays.asList(status3, status4), false, null);
        successStoryResponse = new StatusesResponse(Arrays.asList(status1, status2), false, null);

        failureFeedResponse = new StatusesResponse("Failed to retrieve feed", null);
        failureStoryResponse = new StatusesResponse("Failed to retrieve story", null);

        // Setup a mock ServerFacade that will return known responses
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStatuses(validFeedRequest, StatusesServiceProxy.FEED_URL_PATH)).thenReturn(successFeedResponse);
        Mockito.when(mockServerFacade.getStatuses(validStoryRequest, StatusesServiceProxy.STORY_URL_PATH)).thenReturn(successStoryResponse);

        Mockito.when(mockServerFacade.getStatuses(invalidFeedRequest, StatusesServiceProxy.FEED_URL_PATH)).thenReturn(failureFeedResponse);
        Mockito.when(mockServerFacade.getStatuses(invalidStoryRequest, StatusesServiceProxy.STORY_URL_PATH)).thenReturn(failureStoryResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        statusesServiceProxySpy = Mockito.spy(new StatusesServiceProxy());
        Mockito.when(statusesServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link StatusesServiceProxy#getFeed(FeedRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceProxySpy.getFeed(validFeedRequest);
        Assertions.assertEquals(successFeedResponse, response);

        for (Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    /**
     * Verify that for invalid requests the {@link StatusesServiceProxy#getFeed(FeedRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeed_invalidRequest_failureResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceProxySpy.getFeed(invalidFeedRequest);
        Assertions.assertEquals(failureFeedResponse, response);
    }

    /**
     * Verify that for successful requests the {@link StatusesServiceProxy#getStory(StoryRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceProxySpy.getStory(validStoryRequest);
        Assertions.assertEquals(successStoryResponse, response);

        for (Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    /**
     * Verify that for invalid requests the {@link StatusesServiceProxy#getStory(StoryRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStory_invalidRequest_failureResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceProxySpy.getStory(invalidStoryRequest);
        Assertions.assertEquals(failureStoryResponse, response);
    }
}
