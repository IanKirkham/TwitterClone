package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * Defines the interface for the 'statuses' service.
 */
public interface StatusesService {

    StatusesResponse getStatuses(StatusesRequest request) throws IOException, TweeterRemoteException;
}
