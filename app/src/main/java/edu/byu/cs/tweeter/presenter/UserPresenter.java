package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

/**
 * The presenter for the "Followers/Following" functionality of the application.
 */
public class UserPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public UserPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the users specified in the request. Uses information in
     * the request object to limit the number of users returned and to return the next set of
     * users after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the users.
     */
    public UserResponse getUsers(UserRequest request) throws IOException {
        UserService userService = getUserService();
        return userService.getUsers(request);
    }

    /**
     * Returns an instance of {@link UserService}. Allows mocking of the UserService class
     * for testing purposes. All usages of UserService should get their UserService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    UserService getUserService() {
        return new UserService();
    }
}
