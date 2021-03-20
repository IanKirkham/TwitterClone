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
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StatusesServiceProxyTest {

    private StatusesRequest validFeedRequest;
    private StatusesRequest validStoryRequest;
    private StatusesRequest invalidRequest;

    private StatusesResponse successFeedResponse;
    private StatusesResponse successStoryResponse;
    private StatusesResponse failureResponse;

    private StatusesServiceProxy statusesServiceSpy;

    private static final LocalDateTime time1 = LocalDateTime.now();

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

        currentUser.setFollowees(new ArrayList<String>(Arrays.asList(user1.getAlias())));

        // Setup request objects to use in the tests
        validFeedRequest = new StatusesRequest(currentUser.getFollowees(), 2, null);
        validStoryRequest = new StatusesRequest(new ArrayList<>(Arrays.asList(currentUser.getAlias())), 2, null);

        invalidRequest = new StatusesRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successFeedResponse = new StatusesResponse(Arrays.asList(status1, status2), false);
        successStoryResponse = new StatusesResponse(Arrays.asList(status3, status4), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStatuses(validFeedRequest, StatusesServiceProxy.URL_PATH)).thenReturn(successFeedResponse);
        Mockito.when(mockServerFacade.getStatuses(validStoryRequest, StatusesServiceProxy.URL_PATH)).thenReturn(successStoryResponse);

        failureResponse = new StatusesResponse("An exception occurred");
        Mockito.when(mockServerFacade.getStatuses(invalidRequest, StatusesServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        statusesServiceSpy = Mockito.spy(new StatusesServiceProxy());
        Mockito.when(statusesServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link StatusesService#getStatuses(StatusesRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceSpy.getStatuses(validFeedRequest);
        Assertions.assertEquals(successFeedResponse, response);
    }

    /**
     * Verify that the {@link StatusesService#getStatuses(StatusesRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFeed_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceSpy.getStatuses(validFeedRequest);

        for (Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    /**
     * Verify that for successful requests the {@link StatusesService#getStatuses(StatusesRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceSpy.getStatuses(validStoryRequest);
        Assertions.assertEquals(successStoryResponse, response);
    }

    /**
     * Verify that the {@link StatusesService#getStatuses(StatusesRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetStory_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceSpy.getStatuses(validStoryRequest);

        for (Status status : response.getStatuses()) {
            Assertions.assertNotNull(status.getAuthor().getImageBytes());
        }
    }

    /**
     * Verify that for failed requests the {@link UserService#getUsers(UserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_invalidRequest_returnsNoFollowers() throws IOException, TweeterRemoteException {
        StatusesResponse response = statusesServiceSpy.getStatuses(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
