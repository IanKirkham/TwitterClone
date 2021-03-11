package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.BuildConfig;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "Insert your API invoke URL here";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    // TODO: move the rest of this dummy data to their respective DAO objects
    private static final List<Status> sessionStatuses = new ArrayList<>();

    // TODO: finish refactoring each of these methods so each one uses the clientCommunicator to make requests.
    //  All logic related to generating dummy data should also be put into its respective DAO
    //  (i.e. getStatuses, getStatusesStartingIndex, etc.) all in one DAO
    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        RegisterResponse response = clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LogoutResponse response = clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public UserResponse getUsers(UserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        UserResponse response = clientCommunicator.doPost(urlPath, request, null, UserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public StatusesResponse getStatuses(StatusesRequest request, String urlPath) throws IOException, TweeterRemoteException {
        StatusesResponse response = clientCommunicator.doPost(urlPath, request, null, StatusesResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public PostResponse savePost(PostRequest request, String urlPath) throws IOException, TweeterRemoteException {
        PostResponse response = clientCommunicator.doPost(urlPath, request, null, PostResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowUserResponse followUser(FollowUserRequest followUserRequest) {

        if (followUserRequest.getRootUser().getAlias().equals(followUserRequest.getCurrentUser().getAlias())) {
            return new FollowUserResponse(false, "You cannot follow yourself!");
        }

        // make call to backend
        return new FollowUserResponse(true,"Successfully followed user");
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest unfollowUserRequest) {
        // make call to backend
        return new UnfollowUserResponse(true, "Successfully unfollowed user");
    }
}
