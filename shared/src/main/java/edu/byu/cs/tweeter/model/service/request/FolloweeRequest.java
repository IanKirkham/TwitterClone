package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FolloweeRequest extends UserRequest {
    public FolloweeRequest(String userAlias, int limit, String lastUserAlias, AuthToken authToken) {
        super(userAlias, limit, lastUserAlias, authToken);
    }
    public FolloweeRequest() {
        super();
    }
}
