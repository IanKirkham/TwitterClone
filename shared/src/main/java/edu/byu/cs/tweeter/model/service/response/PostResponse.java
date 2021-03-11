package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostResponse extends Response {

    private Status status;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostResponse(String message) {
        super(false, message);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param status the status that was posted.
     */
    public PostResponse(Status status) {
        super(true, null);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
