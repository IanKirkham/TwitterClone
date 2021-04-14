package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserServiceImplTest {

    private FollowerRequest followerRequest;
    private FolloweeRequest followeeRequest;
    private GetCountRequest getCountRequest;
    private UserRequest userRequest;

    private UserResponse followerResponse;
    private UserResponse followeeResponse;
    private GetCountResponse getCountResponse;
    private UserResponse userResponse;

    private FollowsDAO mockFollowsDAO;
    private UserDAO mockUserDAO;
    private UserServiceImpl userServiceImplSpy;

    private static int PAGE_SIZE = 5;

    @BeforeEach
    public void setup() {
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

        mockUserDAO = Mockito.mock(UserDAO.class);
        mockFollowsDAO = Mockito.mock(FollowsDAO.class);
        Mockito.when(mockFollowsDAO.getFollowers(followerRequest)).thenReturn(followerResponse);
        Mockito.when(mockFollowsDAO.getFollowees(followeeRequest)).thenReturn(followeeResponse);
        Mockito.when(mockFollowsDAO.getFollowerCount(getCountRequest)).thenReturn(getCountResponse);
        Mockito.when(mockFollowsDAO.getFolloweeCount(getCountRequest)).thenReturn(getCountResponse);
        Mockito.when(mockUserDAO.getUser_Authenticated(userRequest)).thenReturn(userResponse);

        userServiceImplSpy = Mockito.spy(UserServiceImpl.class);
        Mockito.when(userServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(userServiceImplSpy.getFollowsDAO()).thenReturn(mockFollowsDAO);
    }

    /**
     * Verify that the {@link UserServiceImpl#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceImplSpy.getFollowers(followerRequest);
        Assertions.assertEquals(followerResponse, response);
    }

    /**
     * Verify that the {@link UserServiceImpl#getFollowees(FolloweeRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceImplSpy.getFollowees(followeeRequest);
        Assertions.assertEquals(followeeResponse, response);
    }

    /**
     * Verify that the {@link UserServiceImpl#getFollowerCount(GetCountRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFollowerCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceImplSpy.getFollowerCount(getCountRequest);
        Assertions.assertEquals(getCountResponse, response);
    }

    /**
     * Verify that the {@link UserServiceImpl#getFolloweeCount(GetCountRequest)}
     * method returns the same result as the {@link FollowsDAO} class.
     */
    @Test
    public void testGetFolloweeCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceImplSpy.getFolloweeCount(getCountRequest);
        Assertions.assertEquals(getCountResponse, response);
    }

    /**
     * Verify that the {@link UserServiceImpl#getUser(UserRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceImplSpy.getUser(userRequest);
        Assertions.assertEquals(userResponse, response);
    }
}

