package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImpl implements LoginService {

    @Override
    public LoginResponse login(LoginRequest request) {
        return getUserDAO().login(request);
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }
}
