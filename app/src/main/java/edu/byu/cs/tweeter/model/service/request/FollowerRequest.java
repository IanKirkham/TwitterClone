package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followers for a specified user.
 */
public class FollowerRequest {

    private final String userAlias;
    private final int limit;
    private final String lastFollowerAlias;

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose followers are to be returned.
     * @param limit the maximum number of followers to return.
     * @param lastFollowerAlias the alias of the last follower that was returned in the previous request (null if
     *                     there was no previous request or if no followers were returned in the
     *                     previous request).
     */
    public FollowerRequest(String userAlias, int limit, String lastFollowerAlias) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    /**
     * Returns the user whose followers are to be returned by this request.
     *
     * @return the user.
     */
    public String getUserAlias() {
        return userAlias;
    }

    /**
     * Returns the number representing the maximum number of followers to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the last follower that was returned in the previous request or null if there was no
     * previous request or if no followers were returned in the previous request.
     *
     * @return the last follower.
     */
    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }
}
