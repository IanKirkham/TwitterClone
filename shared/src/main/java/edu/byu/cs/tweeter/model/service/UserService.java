package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * Defines the interface for the 'user' service
 */
public interface UserService {

    UserResponse getUsers(UserRequest request) throws IOException, TweeterRemoteException;
}
