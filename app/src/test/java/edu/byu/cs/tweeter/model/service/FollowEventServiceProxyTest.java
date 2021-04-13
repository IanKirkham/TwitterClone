package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowEventServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class FollowEventServiceProxyTest {

    private FollowUserRequest validFollowUserRequest;
    private FollowUserRequest invalidFollowUserRequest;
    private UnfollowUserRequest validUnfollowUserRequest;
    private UnfollowUserRequest invalidUnfollowUserRequest;

    private FollowUserResponse successFollowUserResponse;
    private FollowUserResponse failureFollowUserResponse;
    private UnfollowUserResponse successUnfollowUserResponse;
    private UnfollowUserResponse failureUnfollowUserResponse;

    private FollowEventServiceProxy followEventServiceSpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User primaryUser = new User("Test", "User",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User currentUser = new User("Dummy", "User", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validFollowUserRequest = new FollowUserRequest(primaryUser.getAlias(), new AuthToken(), currentUser.getAlias(), primaryUser.getName(), currentUser.getName());
        invalidFollowUserRequest = new FollowUserRequest(null, null, null, null, null);
        validUnfollowUserRequest = new UnfollowUserRequest(primaryUser.getAlias(), new AuthToken(), currentUser.getAlias(), primaryUser.getName(), currentUser.getName());
        invalidUnfollowUserRequest = new UnfollowUserRequest(null, null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successFollowUserResponse = new FollowUserResponse(true, "Successfully followed user");
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.followUser(validFollowUserRequest, FollowEventServiceProxy.FOLLOW_URL_PATH)).thenReturn(successFollowUserResponse);

        failureFollowUserResponse = new FollowUserResponse(false, "Failed to follow user");
        Mockito.when(mockServerFacade.followUser(invalidFollowUserRequest, FollowEventServiceProxy.FOLLOW_URL_PATH)).thenReturn(failureFollowUserResponse);

        successUnfollowUserResponse = new UnfollowUserResponse(true, "Successfully unfollowed user");
        Mockito.when(mockServerFacade.unfollowUser(validUnfollowUserRequest, FollowEventServiceProxy.UNFOLLOW_URL_PATH)).thenReturn(successUnfollowUserResponse);

        failureUnfollowUserResponse = new UnfollowUserResponse(false, "Failed to unfollow user");
        Mockito.when(mockServerFacade.unfollowUser(invalidUnfollowUserRequest, FollowEventServiceProxy.UNFOLLOW_URL_PATH)).thenReturn(failureUnfollowUserResponse);

        // Create a LoginService instance and wrap it with a spy that will use the mock service
        followEventServiceSpy = Mockito.spy(new FollowEventServiceProxy());
        Mockito.when(followEventServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    /**
     * Verify that for successful requests the {@link FollowEventService#followUser(FollowUserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws Exception if an database error occurs.
     */
    @Test
    public void testFollowUser_validRequest_correctResponse() throws Exception {
        FollowUserResponse response = followEventServiceSpy.followUser(validFollowUserRequest);
        Assertions.assertEquals(successFollowUserResponse, response);
    }

    /**
     * Verify that for failed requests the {@link FollowEventService#followUser(FollowUserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testFollowUser_invalidRequest_returnFailure() throws Exception {
        FollowUserResponse response = followEventServiceSpy.followUser(invalidFollowUserRequest);
        Assertions.assertEquals(failureFollowUserResponse, response);
    }

    /**
     * Verify that for successful requests the {@link FollowEventService#unfollowUser(UnfollowUserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     * .
     *
     * @throws Exception if an database error occurs.
     */
    @Test
    public void testUnfollowUser_validRequest_correctResponse() throws Exception {
        UnfollowUserResponse response = followEventServiceSpy.unfollowUser(validUnfollowUserRequest);
        Assertions.assertEquals(successUnfollowUserResponse, response);
    }

    /**
     * Verify that for failed requests the {@link FollowEventService#unfollowUser(UnfollowUserRequest)}
     * method returns the same result as the {@link ServerFacade}.
     *
     * @throws IOException if an IO error occurs.
     */
    @Test
    public void testUnfollowUser_invalidRequest_returnFailure() throws Exception {
        UnfollowUserResponse response = followEventServiceSpy.unfollowUser(invalidUnfollowUserRequest);
        Assertions.assertEquals(failureUnfollowUserResponse, response);
    }
}
