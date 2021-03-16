package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowUserRequest extends FollowEventRequest {
    public UnfollowUserRequest(User primaryUser, AuthToken authToken, User currentUser) {
        super(primaryUser, authToken, currentUser);
    }

    public UnfollowUserRequest() {
        super();
    }
}
