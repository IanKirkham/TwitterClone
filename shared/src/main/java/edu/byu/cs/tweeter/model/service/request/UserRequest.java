package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of users.
 */
public class UserRequest extends AuthenticatedRequest {
    private String userAlias;
    private int limit;
    private String lastUserAlias;

    /**
     * Creates an instance.
     *
     * @param userAlias the alias of the user whose followers/followees are to be returned.
     * @param limit the maximum number of users to return.
     * @param lastUserAlias the alias of the last user that was returned in the previous request (null if
     *                     there was no previous request or if no users were returned in the
     *                     previous request).
     */
    public UserRequest(String userAlias, int limit, String lastUserAlias, AuthToken authToken) {
        super(authToken);
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastUserAlias = lastUserAlias;
    }

    public UserRequest() {}

    /**
     * Returns the users who are to be returned by this request.
     *
     * @return the user.
     */
    public String getUserAlias() {
        return userAlias;
    }

    /**
     * Returns the number representing the maximum number of users to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the last user that was returned in the previous request or null if there was no
     * previous request or if no followers were returned in the previous request.
     *
     * @return the last user.
     */
    public String getLastUserAlias() {
        return lastUserAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastUserAlias(String lastUserAlias) {
        this.lastUserAlias = lastUserAlias;
    }
}
