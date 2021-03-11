package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;

/**
 * Defines the interface for the 'post' service.
 */
public interface PostService {

    PostResponse savePost(PostRequest request) throws IOException, TweeterRemoteException;
}
