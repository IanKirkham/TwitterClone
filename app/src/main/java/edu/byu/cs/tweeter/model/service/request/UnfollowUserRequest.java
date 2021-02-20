package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowUserRequest {

    private final User user;
    private final AuthToken authToken;
    private final User userToUnfollow;

    public UnfollowUserRequest(User user, AuthToken authToken, User userToUnfollow) {
        this.user = user;
        this.authToken = authToken;
        this.userToUnfollow = userToUnfollow;
    }

    public User getUser() {
        return user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getUserToUnfollow() {
        return userToUnfollow;
    }
}
