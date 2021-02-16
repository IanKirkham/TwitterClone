package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

/**
 * Contains the business logic for getting the statuses of a user
 */
public class StatusesService {

    /**
     * Returns the statuses of the specified user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the statuses from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses.
     */
    public StatusesResponse getStory(StatusesRequest request) throws IOException {
        StatusesResponse response = getServerFacade().getStory(request);

        if (response.isSuccess()) {
            loadImages(response); // the profile pictures associated with a status
        }

        return response;
    }

    /**
     * Loads the profile image data for each status included in the response.
     *
     * @param response the response from the followee request.
     */
    private void loadImages(StatusesResponse response) throws IOException {
        if (response.getStatuses().isEmpty()) { return; }
        User user = response.getStatuses().get(0).getAuthor(); // All Statuses in a Story have the same author
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
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
