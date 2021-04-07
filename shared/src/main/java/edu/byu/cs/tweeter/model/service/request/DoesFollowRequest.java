package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class DoesFollowRequest extends FollowEventRequest {
    public DoesFollowRequest(User primaryUser, AuthToken authToken, User currentUser) {
        super(primaryUser, authToken, currentUser);
    }
    public DoesFollowRequest() {
        super();
    }
}
