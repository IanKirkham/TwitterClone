package edu.byu.cs.tweeter.model.service.request;

public class FolloweeRequest extends UserRequest {
    public FolloweeRequest(String userAlias, int limit, String lastUserAlias) {
        super(userAlias, limit, lastUserAlias);
    }
    public FolloweeRequest() {
        super();
    }
}
