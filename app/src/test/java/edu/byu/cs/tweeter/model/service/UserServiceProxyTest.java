package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class UserServiceProxyTest {

    private FollowerRequest validFollowerRequest;
    private FolloweeRequest validFolloweeRequest;
    private GetCountRequest validGetCountRequest;
    private UserRequest validUserRequest;

    private FollowerRequest invalidFollowerRequest;
    private FolloweeRequest invalidFolloweeRequest;
    private GetCountRequest invalidGetCountRequest;
    private UserRequest invalidUserRequest;

    private UserResponse successFollowerResponse;
    private UserResponse successFolloweeResponse;
    private GetCountResponse successGetCountResponse;
    private UserResponse successUserResponse;

    private UserResponse failureFollowerResponse;
    private UserResponse failureFolloweeResponse;
    private GetCountResponse failureGetCountResponse;
    private UserResponse failureUserResponse;

    private UserServiceProxy userServiceProxySpy;

    private static int PAGE_SIZE = 10;

    /**
     * Create a UserService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

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

        // Setup request objects to use in tests
        validFollowerRequest = new FollowerRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        validFolloweeRequest = new FolloweeRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());
        validGetCountRequest = new GetCountRequest(currentUser.getAlias());
        validUserRequest = new UserRequest(currentUser.getAlias(), PAGE_SIZE, null, new AuthToken());

        invalidFollowerRequest = new FollowerRequest(null, 0, null, null);
        invalidFolloweeRequest = new FolloweeRequest(null, 0, null, null);
        invalidGetCountRequest = new GetCountRequest(null);
        invalidUserRequest = new UserRequest(null, 0, null, null);

        // Setup response objects to use in tests
        successFollowerResponse = new UserResponse(currentUser, users, false, null);
        successFolloweeResponse = new UserResponse(currentUser, users, false, null);
        successGetCountResponse = new GetCountResponse(3);
        successUserResponse = new UserResponse(currentUser, null, false, null);

        failureFollowerResponse = new UserResponse("Failed to get followers", null);
        failureFolloweeResponse = new UserResponse("Failed to get followees", null);
        failureGetCountResponse = new GetCountResponse();
        failureUserResponse = new UserResponse("Failed to retreive user", null);


        // Setup a mock ServerFacade that will return known responses
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUsers(validFollowerRequest, UserServiceProxy.FOLLOWER_URL_PATH)).thenReturn(successFollowerResponse);
        Mockito.when(mockServerFacade.getUsers(validFolloweeRequest, UserServiceProxy.FOLLOWEE_URL_PATH)).thenReturn(successFolloweeResponse);
        Mockito.when(mockServerFacade.getFollowCount(validGetCountRequest, UserServiceProxy.FOLLOWER_COUNT_URL_PATH)).thenReturn(successGetCountResponse);
        Mockito.when(mockServerFacade.getFollowCount(validGetCountRequest, UserServiceProxy.FOLLOWEE_COUNT_URL_PATH)).thenReturn(successGetCountResponse);
        Mockito.when(mockServerFacade.getUsers(validUserRequest, UserServiceProxy.GET_USER_URL_PATH)).thenReturn(successUserResponse);

        Mockito.when(mockServerFacade.getUsers(invalidFollowerRequest, UserServiceProxy.FOLLOWER_URL_PATH)).thenReturn(failureFollowerResponse);
        Mockito.when(mockServerFacade.getUsers(invalidFolloweeRequest, UserServiceProxy.FOLLOWEE_URL_PATH)).thenReturn(failureFolloweeResponse);
        Mockito.when(mockServerFacade.getFollowCount(invalidGetCountRequest, UserServiceProxy.FOLLOWER_COUNT_URL_PATH)).thenReturn(failureGetCountResponse);
        Mockito.when(mockServerFacade.getFollowCount(invalidGetCountRequest, UserServiceProxy.FOLLOWEE_COUNT_URL_PATH)).thenReturn(failureGetCountResponse);
        Mockito.when(mockServerFacade.getUsers(invalidUserRequest, UserServiceProxy.GET_USER_URL_PATH)).thenReturn(failureUserResponse);

        // Create a UserServiceProxy instance and wrap it with a spy that will use the mock service
        userServiceProxySpy = Mockito.spy(new UserServiceProxy());
        Mockito.when(userServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link UserServiceProxy#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getFollowers(validFollowerRequest);
        Assertions.assertEquals(successFollowerResponse, response);

        // Ensure profile images loaded
        for(User user : response.getUsers()) {
            Assertions.assertNotNull(user.getImageBytes());
        }
    }

    /**
     * Verify that for failed requests the {@link UserServiceProxy#getFollowers(FollowerRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowers_invalidRequest_returnsNoFollowers() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getFollowers(invalidFollowerRequest);
        Assertions.assertEquals(failureFollowerResponse, response);
    }

    /**
     * Verify that for successful requests the {@link UserServiceProxy#getFollowees(FolloweeRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getFollowees(validFolloweeRequest);
        Assertions.assertEquals(successFolloweeResponse, response);

        // Ensure profile images loaded
        for(User user : response.getUsers()) {
            Assertions.assertNotNull(user.getImageBytes());
        }
    }

    /**
     * Verify that for failed requests the {@link UserServiceProxy#getFollowees(FolloweeRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowees_invalidRequest_returnsNoFollowees() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getFollowees(invalidFolloweeRequest);
        Assertions.assertEquals(failureFolloweeResponse, response);
    }

    /**
     * Verify that for successful requests the {@link UserServiceProxy#getFollowerCount(GetCountRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowerCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceProxySpy.getFollowerCount(validGetCountRequest);
        Assertions.assertEquals(successGetCountResponse, response);
    }

    /**
     * Verify that for failed requests the {@link UserServiceProxy#getFollowerCount(GetCountRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFollowerCount_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceProxySpy.getFollowerCount(invalidGetCountRequest);
        Assertions.assertEquals(failureGetCountResponse, response);
    }

    /**
     * Verify that for successful requests the {@link UserServiceProxy#getFolloweeCount(GetCountRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFolloweeCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceProxySpy.getFolloweeCount(validGetCountRequest);
        Assertions.assertEquals(successGetCountResponse, response);
    }

    /**
     * Verify that for failed requests the {@link UserServiceProxy#getFolloweeCount(GetCountRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetFolloweeCount_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        GetCountResponse response = userServiceProxySpy.getFolloweeCount(invalidGetCountRequest);
        Assertions.assertEquals(failureGetCountResponse, response);
    }

    /**
     * Verify that for successful requests the {@link UserServiceProxy#getUser(UserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getUser(validUserRequest);
        Assertions.assertEquals(successUserResponse, response);
    }

    /**
     * Verify that for failed requests the {@link UserServiceProxy#getFolloweeCount(GetCountRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testGetUser_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        UserResponse response = userServiceProxySpy.getUser(invalidUserRequest);
        Assertions.assertEquals(failureUserResponse, response);
    }
}
