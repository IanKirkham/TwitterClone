package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class UserServiceProxyTest {

    private UserRequest validFollowerRequest;
    private UserRequest validFollowingRequest;
    private UserRequest invalidRequest;

    private UserResponse successFollowerResponse;
    private UserResponse successFollowingResponse;
    private UserResponse failureResponse;

    private UserServiceProxy followerServiceSpy;

    /**
     * Create a UserService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        currentUser.setFollowers(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias())));
        currentUser.setFollowees(new ArrayList<>(Arrays.asList(resultUser1.getAlias(), resultUser2.getAlias(), resultUser3.getAlias())));

        // Setup request objects to use in the tests
        validFollowerRequest = new UserRequest(currentUser.getFollowers(), 2, null);
        validFollowingRequest = new UserRequest(currentUser.getFollowees(), 2, null);

        invalidRequest = new UserRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successFollowerResponse = new UserResponse(Arrays.asList(resultUser1, resultUser2), false);
        successFollowingResponse = new UserResponse(Arrays.asList(resultUser2, resultUser3), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUsers(validFollowerRequest, UserServiceProxy.URL_PATH)).thenReturn(successFollowerResponse);
        Mockito.when(mockServerFacade.getUsers(validFollowingRequest, UserServiceProxy.URL_PATH)).thenReturn(successFollowingResponse);

        failureResponse = new UserResponse("An exception occurred");
        Mockito.when(mockServerFacade.getUsers(invalidRequest, UserServiceProxy.URL_PATH)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followerServiceSpy = Mockito.spy(new UserServiceProxy());
        Mockito.when(followerServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link UserService#getUsers(UserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = followerServiceSpy.getUsers(validFollowerRequest);
        Assertions.assertEquals(successFollowerResponse, response);
    }

    /**
     * Verify that the {@link UserService#getUsers(UserRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        UserResponse response = followerServiceSpy.getUsers(validFollowerRequest);

        for(User user : response.getUsers()) {
            Assertions.assertNotNull(user.getImageBytes());
        }
    }

    /**
     * Verify that for successful requests the {@link UserService#getUsers(UserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = followerServiceSpy.getUsers(validFollowingRequest);
        Assertions.assertEquals(successFollowingResponse, response);
    }

    /**
     * Verify that the {@link UserService#getUsers(UserRequest)} method loads the
     * profile image of each user included in the result.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowees_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        UserResponse response = followerServiceSpy.getUsers(validFollowingRequest);

        for (User user : response.getUsers()) {
            Assertions.assertNotNull(user.getImageBytes());
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
        UserResponse response = followerServiceSpy.getUsers(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
