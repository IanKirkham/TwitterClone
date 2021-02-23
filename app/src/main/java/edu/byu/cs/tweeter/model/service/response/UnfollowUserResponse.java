package edu.byu.cs.tweeter.model.service.response;

public class UnfollowUserResponse extends FollowEventResponse {

    public UnfollowUserResponse(boolean success) {
        super(success);
    }

    public UnfollowUserResponse(boolean success, String message) {
        super(success, message);
    }
}
