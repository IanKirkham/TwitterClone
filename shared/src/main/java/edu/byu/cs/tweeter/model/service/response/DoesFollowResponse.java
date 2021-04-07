package edu.byu.cs.tweeter.model.service.response;

public class DoesFollowResponse extends FollowEventResponse {
    public DoesFollowResponse(boolean success) {
        super(success);
    }

    public DoesFollowResponse(boolean success, String message) {
        super(success, message);
    }
}
