package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

/**
 * Contains the business logic to support the 'register' operation.
 */
public interface RegisterService {
    RegisterResponse register(RegisterRequest request) throws IOException, TweeterRemoteException;
}
