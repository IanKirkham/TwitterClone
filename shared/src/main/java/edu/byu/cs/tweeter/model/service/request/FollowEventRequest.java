package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowEventRequest extends AuthenticatedRequest {
    private User rootUser;
    private User currentUser;

    public FollowEventRequest(User rootUser, AuthToken authToken, User currentUser) {
        super(authToken);
        this.rootUser = rootUser;
        this.currentUser = currentUser;
    }

    public FollowEventRequest() {}

    public User getRootUser() {
        return rootUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
