package edu.byu.cs.tweeter.model.service.request;

import java.time.LocalDateTime;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * statuses for a specified user.
 */
public class StoryRequest {

    private final String userAlias; // used as a key to retrieve the associated Statuses. Maybe better to have User.
    private final int limit;
    private final LocalDateTime lastStatusTimePublished; // used to paginate Responses.

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose statuses are to be returned.
     * @param limit the maximum number of statuses to return.
     * @param lastStatusTimePublished the timePublished of the last Status that was returned in the previous request (null if
     *                                there was no previous request or if no Statuses were returned in the
     *                                previous request).
     */
    public StoryRequest(String userAlias, int limit, LocalDateTime lastStatusTimePublished) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatusTimePublished = lastStatusTimePublished;
    }

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
     * @return the last timePublished.
     */
    public LocalDateTime getLastStatusTimePublished() {
        return lastStatusTimePublished;
    }
}
