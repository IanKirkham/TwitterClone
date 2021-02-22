package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

/**
 * Contains the business logic for getting users
 */
public class UserService {

    /**
     * Returns the users that are specified in the request. Uses information in
     * the request object to limit the number of users returned and to return the next set of
     * users after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the users from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the users.
     */
    public UserResponse getUsers(UserRequest request) throws IOException {
        UserResponse response = getServerFacade().getUsers(request);

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
    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
