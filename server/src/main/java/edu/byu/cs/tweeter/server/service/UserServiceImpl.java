package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserServiceImpl implements UserService {

//    @Override
//    public UserResponse getUsers(UserRequest request) {
//        return getUserDAO().getUsers(request);
//    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }

    @Override
    public UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        return null;
    }

    @Override
    public UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException {
        return null;
    }
}
