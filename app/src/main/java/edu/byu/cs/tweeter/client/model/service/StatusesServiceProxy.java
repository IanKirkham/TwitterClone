package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class StatusesServiceProxy implements StatusesService {

    public static final String URL_PATH = "/statuses";

    /**
     * Returns the statuses of the specified user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request. Uses the {@link ServerFacade} to
     * get the statuses from the server.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses.
     */
    public StatusesResponse getStatuses(StatusesRequest request) throws IOException, TweeterRemoteException {
        StatusesResponse response = getServerFacade().getStatuses(request, URL_PATH);

        if (response.isSuccess()) {
            loadImages(response); // the profile pictures associated with the statuses
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
        Set<User> users = new HashSet<>();
        response.getStatuses().forEach(status -> users.add(status.getAuthor()));

        for (User user : users) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
            // make sure that statuses with the same author share the same reference to that author
            for (Status status : response.getStatuses()) {
                if (status.getAuthor().equals(user)) {
                    status.setAuthor(user);
                }
            }
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
