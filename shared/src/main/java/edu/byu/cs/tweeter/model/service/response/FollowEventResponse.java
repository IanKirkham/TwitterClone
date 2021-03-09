package edu.byu.cs.tweeter.model.service.response;

public class FollowEventResponse extends Response {
    public FollowEventResponse(boolean success) {
        super(success);
    }

    public FollowEventResponse(boolean success, String message) {
        super(success, message);
    }
}
