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
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowEventServiceImpl implements FollowEventService {
    @Override
    public FollowUserResponse followUser(FollowUserRequest request) {
        //return getUserDAO().followUser(request);
        return null;
    }

    @Override
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        //return getUserDAO().unfollowUser(request);
        return null;
    }

    @Override
    public DoesFollowResponse doesFollowUser(DoesFollowRequest request) throws IOException, TweeterRemoteException {
        return null;
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
