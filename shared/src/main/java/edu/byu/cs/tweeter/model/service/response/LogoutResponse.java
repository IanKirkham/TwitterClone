package edu.byu.cs.tweeter.model.service.response;

/**
 * A response for a {@link edu.byu.cs.tweeter.model.service.request.LogoutRequest}.
 */
public class LogoutResponse extends Response {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public LogoutResponse(String message) {
        super(false, message); // FIXME: It's weird that this is false by default. We even check this property later,
                                      // FIXME: it just happens that the check doesn't really do much
    }
}
