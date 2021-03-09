package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostResponse {
    private boolean wasSuccessful;
    private Status status;

    public PostResponse(boolean wasSuccessful, Status status) {
        this.wasSuccessful = wasSuccessful;
        this.status = status;
    }

    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    public Status getStatus() {
        return status;
    }
}
