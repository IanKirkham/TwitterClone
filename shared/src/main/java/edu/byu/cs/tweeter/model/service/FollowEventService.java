package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public interface FollowEventService {

    FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException;
    UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException;
}
