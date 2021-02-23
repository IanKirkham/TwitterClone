package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class FollowEventPresenterTest {


    private FollowUserRequest followUserRequest;
    private UnfollowUserRequest unfollowUserRequest;
    private FollowUserResponse followUserResponse;
    private UnfollowUserResponse unfollowUserResponse;
    private FollowEventService mockFollowEventService;
    private FollowEventPresenter presenter;

    private boolean viewWasCalled = false;

    @BeforeEach
    public void setup() throws IOException {

        User primaryUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User currentUser = new User("Dummy", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        followUserRequest = new FollowUserRequest(primaryUser, new AuthToken(), currentUser);
        unfollowUserRequest = new UnfollowUserRequest(primaryUser, new AuthToken(), currentUser);

        followUserResponse = new FollowUserResponse(true, "Successfully followed user");
        unfollowUserResponse = new UnfollowUserResponse(true, "Successfully unfollowed user");

        // Create a mock FollowEventService
        mockFollowEventService = Mockito.mock(FollowEventService.class);
        Mockito.when(mockFollowEventService.followUser(followUserRequest)).thenReturn(followUserResponse);
        Mockito.when(mockFollowEventService.unfollowUser(unfollowUserRequest)).thenReturn(unfollowUserResponse);

        // Wrap a FollowEventPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new FollowEventPresenter(new FollowEventPresenter.View() {
            public void requestSuccessful(FollowEventResponse response) {
                viewWasCalled = true;
            }
            public void requestUnsuccessful(FollowEventResponse response) {
                viewWasCalled = true;
            }
            public void handleException(Exception exception) {}
        }));
        Mockito.when(presenter.getFollowEventService()).thenReturn(mockFollowEventService);

        viewWasCalled = false;
    }

    @Test
    public void testFollowUser_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowEventService.followUser(followUserRequest)).thenReturn(followUserResponse);
        Assertions.assertEquals(followUserResponse, presenter.followUser(followUserRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws IOException {
        presenter.requestSuccessful(followUserResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testUnfollowUser_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowEventService.unfollowUser(unfollowUserRequest)).thenReturn(unfollowUserResponse);
        Assertions.assertEquals(unfollowUserResponse, presenter.unfollowUser(unfollowUserRequest));
    }

    @Test
    public void testFollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowEventService.followUser(followUserRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.followUser(followUserRequest);
        });
    }

    @Test
    public void testUnfollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowEventService.unfollowUser(unfollowUserRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.unfollowUser(unfollowUserRequest);
        });
    }
}
