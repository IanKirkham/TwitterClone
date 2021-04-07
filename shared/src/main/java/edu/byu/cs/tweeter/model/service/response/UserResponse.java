package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * A paged response for a {@link edu.byu.cs.tweeter.model.service.request.UserRequest}.
 */
public class UserResponse extends PagedResponse {
    private User queriedUser;
    private List<User> users;

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public UserResponse(String message) {
        super(false, message, false);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param users the followees to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    public UserResponse(User queriedUser, List<User> users, boolean hasMorePages) {
        super(true, hasMorePages);
        this.users = users;
    }

    /**
     * Returns the followees for the corresponding request.
     *
     * @return the followees.
     */
    public List<User> getUsers() {
        return users;
    }

    public User getQueriedUser() {
        return queriedUser;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        UserResponse that = (UserResponse) param;

        return (Objects.equals(users, that.users) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(users);
    }
}
