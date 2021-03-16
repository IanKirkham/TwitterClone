package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowEventRequest {

    private User rootUser;
    private AuthToken authToken;
    private User currentUser;

    public FollowEventRequest(User rootUser, AuthToken authToken, User currentUser) {
        this.rootUser = rootUser;
        this.authToken = authToken;
        this.currentUser = currentUser;
    }

    public FollowEventRequest() {}

    public User getRootUser() {
        return rootUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
