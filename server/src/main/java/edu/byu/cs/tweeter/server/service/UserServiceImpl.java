package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUsers(UserRequest request) {
        return getUserDAO().getUsers(request);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
