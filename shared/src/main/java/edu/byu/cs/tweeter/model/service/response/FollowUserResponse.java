package edu.byu.cs.tweeter.model.service.response;

public class FollowUserResponse extends FollowEventResponse {
    public FollowUserResponse(boolean success) {
        super(success);
    }

    public FollowUserResponse(boolean success, String message) {
        super(success, message);
    }
}
