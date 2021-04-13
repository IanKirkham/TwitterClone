package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class FollowEventServiceProxy implements FollowEventService {

    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";
    public static final String DOES_FOLLOW_URL_PATH = "/users/doesfollow";

    public FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.followUser(request, FOLLOW_URL_PATH);
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.unfollowUser(request, UNFOLLOW_URL_PATH);
    }

    public DoesFollowResponse doesFollowUser(DoesFollowRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        return serverFacade.doesFollowUser(request, DOES_FOLLOW_URL_PATH);
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
