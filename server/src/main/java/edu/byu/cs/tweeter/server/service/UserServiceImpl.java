package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserServiceImpl implements UserService {
    @Override
    public UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFollowers(request);
    }

    @Override
    public UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFollowees(request);
    }

    @Override
    public GetCountResponse getFolloweeCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFolloweeCount(request);
    }

    @Override
    public GetCountResponse getFollowerCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getFollowsDAO().getFollowerCount(request);
    }

    @Override
    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        return getUserDAO().getUser_Authenticated(request);
    }

    FollowsDAO getFollowsDAO() {
        return new FollowsDAO();
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
