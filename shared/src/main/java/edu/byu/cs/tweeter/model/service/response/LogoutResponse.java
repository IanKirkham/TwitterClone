package edu.byu.cs.tweeter.model.service.response;

/**
 * A response for a {@link edu.byu.cs.tweeter.model.service.request.LogoutRequest}.
 */
public class LogoutResponse extends Response {

    public LogoutResponse(boolean success, String message) {
        super(success, message);
    }
}
