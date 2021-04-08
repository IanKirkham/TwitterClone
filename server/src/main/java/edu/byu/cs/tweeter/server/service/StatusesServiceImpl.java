package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.StatusesDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusesServiceImpl implements StatusesService {
//    @Override
//    public StatusesResponse getStatuses(StatusesRequest request) {
//        return getStatusesDAO().getStatuses(request);
//    }

    StatusesDAO getStatusesDAO() {
        return new StatusesDAO();
    }

    @Override
    public StatusesResponse getStory(StoryRequest request) {
        return new StoryDAO().getStory(request);
    }

    @Override
    public StatusesResponse getFeed(FeedRequest request) {
        return null;
    }
}
