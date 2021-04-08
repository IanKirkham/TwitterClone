package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedRequest {
    private AuthToken authToken;

    public AuthenticatedRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    public AuthenticatedRequest() {}

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
