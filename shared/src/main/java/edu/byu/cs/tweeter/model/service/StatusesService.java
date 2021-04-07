package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * Defines the interface for the 'statuses' service.
 */
public interface StatusesService {
    StatusesResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException;
    StatusesResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException;
}
