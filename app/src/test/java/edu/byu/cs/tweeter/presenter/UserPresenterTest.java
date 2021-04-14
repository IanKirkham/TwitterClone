package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class UserPresenterTest {

    private FollowerRequest followerRequest;
    private FolloweeRequest followeeRequest;
    private GetCountRequest getCountRequest;
    private UserRequest userRequest;

    private UserResponse followerResponse;
    private UserResponse followeeResponse;
    private GetCountResponse getCountResponse;
    private UserResponse userResponse;
    private UserServiceProxy mockUserService;
    private UserPresenter presenter;

    private boolean viewWasCalled = false;
    private static int PAGE_SIZE = 5;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        List<User> users = new ArrayList<>();
        users.add(resultUser1);
        users.add(resultUser2);
        users.add(resultUser3);

        followerRequest = new FollowerRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        followeeRequest = new FolloweeRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        getCountRequest = new GetCountRequest(currentUser.getAlias());
        userRequest = new UserRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());

        followerResponse = new UserResponse(currentUser, users, false, null);
        followeeResponse = new UserResponse(currentUser, users, false, null);
        getCountResponse = new GetCountResponse(3);
        userResponse = new UserResponse(currentUser, null, false, null);

        // Create a mock UserService
        mockUserService = Mockito.mock(UserServiceProxy.class);
        Mockito.when(mockUserService.getFollowers(followerRequest)).thenReturn(followerResponse);
        Mockito.when(mockUserService.getFollowees(followeeRequest)).thenReturn(followeeResponse);
        Mockito.when(mockUserService.getFollowerCount(getCountRequest)).thenReturn(getCountResponse);
        Mockito.when(mockUserService.getFolloweeCount(getCountRequest)).thenReturn(getCountResponse);
        Mockito.when(mockUserService.getUser(userRequest)).thenReturn(userResponse);

        // Wrap a UserPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new UserPresenter(new UserPresenter.View() {
            @Override
            public void presentNewUserView(User user) {
                viewWasCalled = true;
            }

            @Override
            public void updateFollowerCount(int count) {
                viewWasCalled = true;
            }

            @Override
            public void updateFolloweeCount(int count) {
                viewWasCalled = true;
            }
        }));
        Mockito.when(presenter.getUserService()).thenReturn(mockUserService);

        viewWasCalled = false;
    }

    @Test
    public void testGetFollowers_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getFollowers(followerRequest)).thenReturn(followerResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(followerResponse, presenter.getFollowers(followerRequest));
    }

    @Test
    public void testGetFollowees_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getFollowees(followeeRequest)).thenReturn(followeeResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(followeeResponse, presenter.getFollowees(followeeRequest));
    }

    @Test
    public void testGetFollowerCount_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getFollowerCount(getCountRequest)).thenReturn(getCountResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(getCountResponse, presenter.getFollowerCount(getCountRequest));
    }

    @Test
    public void testGetFolloweeCount_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getFolloweeCount(getCountRequest)).thenReturn(getCountResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(getCountResponse, presenter.getFolloweeCount(getCountRequest));
    }

    @Test
    public void testGetUser_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getUser(userRequest)).thenReturn(userResponse);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(userResponse, presenter.getUser(userRequest));
    }

    @Test
    public void testGetFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockUserService.getFollowers(followerRequest)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowers(followerRequest);
        });
    }

    @Test
    public void testViewMethod_wasCalled() throws IOException {
        presenter.usersRetrieved(userResponse);
        Assertions.assertTrue(viewWasCalled);
    }
}
