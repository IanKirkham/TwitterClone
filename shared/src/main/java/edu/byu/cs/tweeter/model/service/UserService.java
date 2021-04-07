package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Defines the interface for the 'user' service
 */
public interface UserService {
    UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException;
    UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException;
}
