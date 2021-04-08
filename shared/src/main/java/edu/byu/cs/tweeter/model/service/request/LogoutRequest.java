package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a logout request.
 */
public class LogoutRequest extends AuthenticatedRequest {
    private User user;

    /**
     * Creates an instance.
     *
     * @param user the user to be logged out.
     * @param authToken the password of the user to be logged out.
     */
    public LogoutRequest(User user, AuthToken authToken) {
        super(authToken);
        this.user = user;
    }

    public LogoutRequest() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
