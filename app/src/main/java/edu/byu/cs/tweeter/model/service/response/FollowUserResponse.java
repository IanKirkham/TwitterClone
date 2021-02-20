package edu.byu.cs.tweeter.model.service.response;

public class FollowUserResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowUserResponse(String message) {
        super(false, message);
    }
}
