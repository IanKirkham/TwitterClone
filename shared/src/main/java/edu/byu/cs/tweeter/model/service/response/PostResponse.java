package edu.byu.cs.tweeter.model.service.response;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostResponse extends Response {

    private Status status;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostResponse(boolean success, String message) {
        super(success, message);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PostResponse that = (PostResponse) o;
        return Objects.equals(status, that.status);
    }
}
