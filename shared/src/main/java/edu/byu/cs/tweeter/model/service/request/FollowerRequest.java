package edu.byu.cs.tweeter.model.service.request;

public class FollowerRequest extends UserRequest {
    public FollowerRequest(String userAlias, int limit, String lastUserAlias) {
        super(userAlias, limit, lastUserAlias);
    }
    public FollowerRequest() {
        super();
    }
}
