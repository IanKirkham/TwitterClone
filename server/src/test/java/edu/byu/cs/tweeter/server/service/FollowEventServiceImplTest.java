package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowEventServiceImplTest {

    private FollowUserRequest followRequest;
    private FollowUserResponse expectedFollowResponse;
    private UnfollowUserRequest unfollowRequest;
    private UnfollowUserResponse expectedUnfollowUserResponse;
    private UserDAO mockUserDAO;
    private FollowEventServiceImpl followEventServiceImplSpy;

    @BeforeEach
    public void setup() {

        User primaryUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User currentUser = new User("Dummy", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        followRequest = new FollowUserRequest(primaryUser, new AuthToken(), currentUser);
        unfollowRequest = new UnfollowUserRequest(primaryUser, new AuthToken(), currentUser);

        // Setup a mock UserDAO that will return known responses
        expectedFollowResponse = new FollowUserResponse(true, "Successfully followed user");
        expectedUnfollowUserResponse = new UnfollowUserResponse(true, "Successfully unfollowed user");
        mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.followUser(followRequest)).thenReturn(expectedFollowResponse);
        Mockito.when(mockUserDAO.unfollowUser(unfollowRequest)).thenReturn(expectedUnfollowUserResponse);

        followEventServiceImplSpy = Mockito.spy(FollowEventServiceImpl.class);
        Mockito.when(followEventServiceImplSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    /**
     * Verify that the {@link FollowEventServiceImpl#followUser(FollowUserRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testFollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowUserResponse response = followEventServiceImplSpy.followUser(followRequest);
        Assertions.assertEquals(expectedFollowResponse, response);
    }

    /**
     * Verify that the {@link FollowEventServiceImpl#unfollowUser(UnfollowUserRequest)}
     * method returns the same result as the {@link UserDAO} class.
     */
    @Test
    public void testUnfollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowUserResponse response = followEventServiceImplSpy.unfollowUser(unfollowRequest);
        Assertions.assertEquals(expectedUnfollowUserResponse, response);
    }
}

