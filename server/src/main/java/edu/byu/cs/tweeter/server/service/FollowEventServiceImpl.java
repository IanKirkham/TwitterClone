package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowEventServiceImpl implements FollowEventService {
    @Override
    public FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().followUser(request);
    }

    @Override
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().unfollowUser(request);
    }

    @Override
    public DoesFollowResponse doesFollowUser(DoesFollowRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().doesFollowUser(request);
    }

    FollowsDAO getFollowsDAO() {
        return new FollowsDAO();
    }
}
