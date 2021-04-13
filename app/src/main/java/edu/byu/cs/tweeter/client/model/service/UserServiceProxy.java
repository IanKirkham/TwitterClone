package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class UserServiceProxy implements UserService {
    public static final String FOLLOWER_URL_PATH = "/users/followers";
    public static final String FOLLOWEE_URL_PATH = "/users/followees";
    public static final String FOLLOWER_COUNT_URL_PATH = "/users/followers/count";
    public static final String FOLLOWEE_COUNT_URL_PATH = "/users/followees/count";
    public static final String GET_USER_URL_PATH = "/users";

    /**
     * Returns the users that are specified in the request. Uses information in
     * the request object to limit the number of users returned and to return the next set of
     * users after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the users from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the users.
     */
    public UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        UserResponse response = getServerFacade().getUsers(request, FOLLOWER_URL_PATH);

        if (response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    /**
     * Returns the users that are specified in the request. Uses information in
     * the request object to limit the number of users returned and to return the next set of
     * users after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the users from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the users.
     */
    public UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException {
        UserResponse response = getServerFacade().getUsers(request, FOLLOWEE_URL_PATH);

        if (response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    @Override
    public GetCountResponse getFolloweeCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getServerFacade().getFollowCount(request, FOLLOWEE_COUNT_URL_PATH);
    }

    @Override
    public GetCountResponse getFollowerCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getServerFacade().getFollowCount(request, FOLLOWER_COUNT_URL_PATH);
    }

    @Override
    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        UserResponse response = getServerFacade().getUsers(request, GET_USER_URL_PATH);

        if (response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    /**
     * Loads the profile image data for each user included in the response.
     *
     * @param response the response from the follower request.
     */
    private void loadImages(UserResponse response) throws IOException {
        if (response.getQueriedUser() != null && response.getQueriedUser().getImageBytes() == null) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(response.getQueriedUser().getImageUrl());
            response.getQueriedUser().setImageBytes(bytes);
        }

        if (response.getUsers() == null) {
            return;
        }

        for (User user : response.getUsers()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
