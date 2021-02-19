package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a logout request.
 */
public class LogoutRequest {

    private final User user;
    private final AuthToken authToken;

    /**
     * Creates an instance.
     *
     * @param user the user to be logged out.
     * @param authToken the password of the user to be logged out.
     */
    public LogoutRequest(User user, AuthToken authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
