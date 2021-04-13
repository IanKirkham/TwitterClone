package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Defines the interface for the 'user' service
 */
public interface UserService {
    UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException;
    UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException;
    GetCountResponse getFolloweeCount(GetCountRequest request) throws IOException, TweeterRemoteException;
    GetCountResponse getFollowerCount(GetCountRequest request) throws IOException, TweeterRemoteException;
    UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException;
}
