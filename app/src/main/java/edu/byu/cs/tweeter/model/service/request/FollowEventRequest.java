package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowEventRequest {

    private final User rootUser;
    private final AuthToken authToken;
    private final User currentUser;

    public FollowEventRequest(User rootUser, AuthToken authToken, User currentUser) {
        this.rootUser = rootUser;
        this.authToken = authToken;
        this.currentUser = currentUser;
    }

    public User getRootUser() {
        return rootUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
