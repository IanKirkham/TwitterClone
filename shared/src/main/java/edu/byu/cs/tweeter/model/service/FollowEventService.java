package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class FollowEventService {

    public FollowUserResponse followUser(FollowUserRequest request) throws IOException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.followUser(request);
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.unfollowUser(request);
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
