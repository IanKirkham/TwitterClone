package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class FollowEventServiceImpl implements FollowEventService {
    @Override
    public FollowUserResponse followUser(FollowUserRequest request) {
        return new FollowUserResponse(true, "Successfully followed user");
    }

    @Override
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        return new UnfollowUserResponse(true, "Successfully unfollowed user");
    }
}
