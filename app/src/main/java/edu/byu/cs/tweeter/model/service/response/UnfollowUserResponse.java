package edu.byu.cs.tweeter.model.service.response;

public class UnfollowUserResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UnfollowUserResponse(String message) {
        super(false, message);
    }
}
