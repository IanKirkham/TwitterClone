package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;

public class UserServiceImpl implements UserService {
    @Override
    public UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFollowers(request);
    }

    @Override
    public UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFollowees(request);
    }

    FollowsDAO getFollowsDAO() {
        return new FollowsDAO();
    }
}
