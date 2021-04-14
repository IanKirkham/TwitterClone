package edu.byu.cs.tweeter.presenter;

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
import edu.byu.cs.tweeter.client.presenter.StatusesPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StatusesPresenterTest {

    private FeedRequest feedRequest;
    private StoryRequest storyRequest;
    private StatusesResponse feedResponse;
    private StatusesResponse storyResponse;
    private StatusesServiceProxy mockStatusesService;
    private StatusesPresenter presenter;

    private boolean viewWasCalled = false;

    private static final LocalDateTime time1 = LocalDateTime.now();
    private static int PAGE_SIZE = 5;

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


        feedRequest = new FeedRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        storyRequest = new StoryRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());

        feedResponse = new StatusesResponse(Arrays.asList(status3, status4), false, null);
        storyResponse = new StatusesResponse(Arrays.asList(status1, status2), false, null);

        // Create a mock UserService
        mockStatusesService = Mockito.mock(StatusesServiceProxy.class);
        Mockito.when(mockStatusesService.getFeed(feedRequest)).thenReturn(feedResponse);
        Mockito.when(mockStatusesService.getStory(storyRequest)).thenReturn(storyResponse);

        // Wrap a UserPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new StatusesPresenter(new StatusesPresenter.View() {
            public void statusesRetrieved(StatusesResponse statusesResponse) {
                viewWasCalled = true;
            }
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getStatusesService()).thenReturn(mockStatusesService);

        viewWasCalled = false;
    }

    @Test
    public void testGetFeed_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStatusesService.getFeed(feedRequest)).thenReturn(feedResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(feedResponse, presenter.getFeed(feedRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws IOException {
        presenter.statusesRetrieved(feedResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStatusesService.getStory(storyRequest)).thenReturn(storyResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(storyResponse, presenter.getStory(storyRequest));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockStatusesService.getFeed(feedRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFeed(feedRequest);
        });
    }
}
