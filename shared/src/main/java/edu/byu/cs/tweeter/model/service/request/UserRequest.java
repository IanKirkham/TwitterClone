package edu.byu.cs.tweeter.model.service.request;

import java.util.List;

/**
 * Contains all the information needed to make a request to have the server return the next page of users.
 */
public class UserRequest {

    private List<String> userAliases;
    private int limit;
    private String lastUserAlias;

    /**
     * Creates an instance.
     *
     * @param userAliases the aliases of the users who are to be returned.
     * @param limit the maximum number of users to return.
     * @param lastUserAlias the alias of the last user that was returned in the previous request (null if
     *                     there was no previous request or if no users were returned in the
     *                     previous request).
     */
    public UserRequest(List<String> userAliases, int limit, String lastUserAlias) {
        this.userAliases = userAliases;
        this.limit = limit;
        this.lastUserAlias = lastUserAlias;
    }

    public UserRequest() {}

    /**
     * Returns the users who are to be returned by this request.
     *
     * @return the user.
     */
    public List<String> getUserAliases() {
        return userAliases;
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

    public void setUserAliases(List<String> userAliases) {
        this.userAliases = userAliases;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastUserAlias(String lastUserAlias) {
        this.lastUserAlias = lastUserAlias;
    }
}
