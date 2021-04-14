package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusesServiceImplTest {

    private FeedRequest feedRequest;
    private StoryRequest storyRequest;
    private StatusesResponse expectedResponse;
    private FeedDAO mockFeedDAO;
    private StoryDAO mockStoryDAO;
    private StatusesServiceImpl statusesServiceImplSpy;

    private static final LocalDateTime time1 = LocalDateTime.now();
    private static int PAGE_SIZE = 10;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User user1 = new User("Allen", "Anderson", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        String content1 = "Content: Hello World! \uD83D\uDE03, Mentions: @BobBobson, URLs: http://www.google.com";
        String content2 = "Content: Hello World!, Mentions: @BobBobson, URLs: https://www.google.com";

        Status status1 = new Status(content1, currentUser, time1);
        Status status2 = new Status(content2, currentUser, time1.plus(Duration.ofSeconds(1)));

        // Setup request objects to use in the tests
        feedRequest = new FeedRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        storyRequest = new StoryRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());

        // Setup a mock UserDAO that will return known responses
        expectedResponse = new StatusesResponse(Arrays.asList(status1, status2), false, null);

        mockFeedDAO = Mockito.mock(FeedDAO.class);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockFeedDAO.getFeed(feedRequest)).thenReturn(expectedResponse);
        Mockito.when(mockStoryDAO.getStory(storyRequest)).thenReturn(expectedResponse);

        statusesServiceImplSpy = Mockito.spy(StatusesServiceImpl.class);
        Mockito.when(statusesServiceImplSpy.getFeedDAO()).thenReturn(mockFeedDAO);
        Mockito.when(statusesServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    /**
     * Verify that the {@link StatusesServiceImpl#getFeed(FeedRequest)}
     * method returns the same result as the {@link FeedDAO} class.
     */
    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceImplSpy.getFeed(feedRequest);
        Assertions.assertEquals(expectedResponse, response);
    }

    /**
     * Verify that the {@link StatusesServiceImpl#getStory(StoryRequest)}
     * method returns the same result as the {@link StoryDAO} class.
     */
    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceImplSpy.getStory(storyRequest);
        Assertions.assertEquals(expectedResponse, response);
    }
}

