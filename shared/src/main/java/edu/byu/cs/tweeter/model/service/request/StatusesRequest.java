package edu.byu.cs.tweeter.model.service.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user.
 */
public class StatusesRequest {

    private final List<String> userAliases;
    private final int limit;
    private final Status lastStatus; // used to paginate Responses.

    /**
     * Creates an instance.
     *
     * @param userAliases the aliases of the users whose statuses are to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last Status that was returned in the previous request (null if
     *                                there was no previous request or if no Statuses were returned in the
     *                                previous request).
     */
    public StatusesRequest(List<String> userAliases, int limit, Status lastStatus) {
        this.userAliases = userAliases;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    /**
     * Returns the user whose statuses are to be returned by this request.
     *
     * @return the user.
     */
    public List<String> getUserAliases() {
        return userAliases;
    }

    /**
     * Returns the number representing the maximum number of statuses to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the last timePublished that was returned in the previous request or null if there was no
     * previous request or if no statuses were returned in the previous request.
     *
     * @return the last status.
     */
    public Status getLastStatus() {
        return lastStatus;
    }
}
