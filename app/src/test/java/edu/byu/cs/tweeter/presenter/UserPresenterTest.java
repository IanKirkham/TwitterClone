package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class UserPresenterTest {

    private UserRequest followingRequest;
    private UserRequest followersRequest;
    private UserResponse followingResponse;
    private UserResponse followersResponse;
    private UserResponse singleUserResponse;
    private UserServiceProxy mockUserService;
    private UserPresenter presenter;

    private boolean viewWasCalled = false;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        currentUser.setFollowees(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias())));
        currentUser.setFollowers(new ArrayList<>(Arrays.asList(resultUser2.getAlias(), resultUser3.getAlias())));

        followingRequest = new UserRequest(currentUser.getFollowees(), 2, null);
        followingResponse = new UserResponse(Arrays.asList(resultUser1, resultUser2), false);

        followersRequest = new UserRequest(currentUser.getFollowers(), 2, null);
        followersResponse = new UserResponse(Arrays.asList(resultUser2, resultUser3), false);

        singleUserResponse = new UserResponse(Arrays.asList(resultUser1), false);

        // Create a mock UserService
        mockUserService = Mockito.mock(UserServiceProxy.class);
        Mockito.when(mockUserService.getUsers(followingRequest)).thenReturn(followingResponse);
        Mockito.when(mockUserService.getUsers(followersRequest)).thenReturn(followersResponse);

        // Wrap a UserPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {
            @Override
            public void presentNewUserView(User user) {
                viewWasCalled = true;
            }
        }));
        Mockito.when(presenter.getUserService()).thenReturn(mockUserService);

        viewWasCalled = false;
    }

    @Test
    public void testGetFollowing_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getUsers(followingRequest)).thenReturn(followingResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(followingResponse, presenter.getUsers(followingRequest));
    }

    @Test
    public void testViewMethod_wasCalled() throws IOException {
        presenter.usersRetrieved(singleUserResponse);
        Assertions.assertTrue(viewWasCalled);
    }

    @Test
    public void testGetFollowers_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getUsers(followersRequest)).thenReturn(followersResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(followersResponse, presenter.getUsers(followersRequest));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getUsers(followersRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getUsers(followersRequest);
        });
    }
}
