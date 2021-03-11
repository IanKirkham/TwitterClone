package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

/**
 * Defines the interface for the 'logout' service
 */
public interface LogoutService {

    LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException;
}
