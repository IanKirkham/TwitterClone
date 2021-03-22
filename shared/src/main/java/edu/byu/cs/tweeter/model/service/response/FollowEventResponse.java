package edu.byu.cs.tweeter.model.service.response;

public class FollowEventResponse extends Response {
    public FollowEventResponse(boolean success) {
        super(success);
    }

    public FollowEventResponse(boolean success, String message) {
        super(success, message);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FollowEventResponse)) {
            return false;
        }
        FollowEventResponse f = (FollowEventResponse) o;
        return isSuccess() == f.isSuccess() && getMessage().equals(f.getMessage());
    }
}
