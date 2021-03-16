package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FollowEventServiceImpl implements FollowEventService {
    @Override
    public FollowUserResponse followUser(FollowUserRequest request) {
        return getUserDAO().followUser(request);
    }

    @Override
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        return getUserDAO().unfollowUser(request);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
