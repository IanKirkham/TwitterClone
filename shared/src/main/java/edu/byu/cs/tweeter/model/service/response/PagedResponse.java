package edu.byu.cs.tweeter.model.service.response;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public class PagedResponse extends Response {

    private final boolean hasMorePages;
    private final String lastKey;

    PagedResponse(boolean success, boolean hasMorePages, String lastKey) {
        super(success);
        this.hasMorePages = hasMorePages;
        this.lastKey = lastKey;
    }

    PagedResponse(boolean success, String message, boolean hasMorePages, String lastKey) {
        super(success, message);
        this.hasMorePages = hasMorePages;
        this.lastKey = lastKey;
    }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public String getLastKey() {
        return lastKey;
    }
}
