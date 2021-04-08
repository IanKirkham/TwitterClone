package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusesServiceImpl implements StatusesService {
    @Override
    public StatusesResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        return getStoryDAO().getStory(request);
    }

    @Override
    public StatusesResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        return getFeedDAO().getFeed(request);
    }

    FeedDAO getFeedDAO() {
        return new FeedDAO();
    }

    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
