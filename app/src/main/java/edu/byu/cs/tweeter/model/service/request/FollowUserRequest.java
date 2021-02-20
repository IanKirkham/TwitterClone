package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowUserRequest {

    private final User user;
    private final AuthToken authToken;
    private final User userToFollow;

    public FollowUserRequest(User user, AuthToken authToken, User userToFollow) {
        this.user = user;
        this.authToken = authToken;
        this.userToFollow = userToFollow;
    }

    public User getUser() {
        return user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getUserToFollow() {
        return userToFollow;
    }
}
