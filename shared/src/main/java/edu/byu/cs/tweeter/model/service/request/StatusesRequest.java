package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user.
 */
public class StatusesRequest {
    private String userAlias;
    private int limit;
    private Status lastStatus; // used to paginate Responses.

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose story/feed is to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatus the last Status that was returned in the previous request (null if
     *                                there was no previous request or if no Statuses were returned in the
     *                                previous request).
     */
    public StatusesRequest(String userAlias, int limit, Status lastStatus) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public StatusesRequest() {}

    /**
     * Returns the user whose statuses are to be returned by this request.
     *
     * @return the user.
     */
    public String getUserAlias() {
        return userAlias;
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

    public void setUserAliases(String userAlias) {
        this.userAlias = userAlias;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
