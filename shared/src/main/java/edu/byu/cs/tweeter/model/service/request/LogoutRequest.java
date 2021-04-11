package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a logout request.
 */
public class LogoutRequest extends AuthenticatedRequest {
    private String userAlias;

    /**
     * Creates an instance.
     *
     * @param userAlias the user to be logged out.
     * @param authToken the password of the user to be logged out.
     */
    public LogoutRequest(String userAlias, AuthToken authToken) {
        super(authToken);
        this.userAlias = userAlias;
    }

    public LogoutRequest() {}

    public String getUser() {
        return userAlias;
    }

    public void setUser(String user) {
        this.userAlias = userAlias;
    }
}
