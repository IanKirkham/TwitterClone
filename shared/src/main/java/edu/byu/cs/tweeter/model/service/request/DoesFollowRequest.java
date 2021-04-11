package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class DoesFollowRequest extends FollowEventRequest {
    public DoesFollowRequest(String primaryUserAlias, AuthToken authToken, String currentUserAlias, String primaryUserName, String currentUserName) {
        super(primaryUserAlias, authToken, currentUserAlias, primaryUserName, currentUserName);
    }
    public DoesFollowRequest() {
        super();
    }
}
