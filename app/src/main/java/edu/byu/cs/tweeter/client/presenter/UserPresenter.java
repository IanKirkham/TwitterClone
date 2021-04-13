package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserServiceProxy;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.UserService;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUsersTask;

/**
 * The presenter for the "Followers/Following" functionality of the application.
 */
public class UserPresenter implements GetUserTask.Observer, GetFollowCountTask.Observer {

    private final View view;

    public void usersRetrieved(UserResponse userResponse) {
        if (view != null && userResponse.getQueriedUser() != null) {
            view.presentNewUserView(userResponse.getQueriedUser());
        }
    }

    @Override
    public void handleException(Exception exception) {}

    @Override
    public void updateFollowerCount(int count) {
        view.updateFollowerCount(count);
    }

    @Override
    public void updateFolloweeCount(int count) {
        view.updateFolloweeCount(count);
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void presentNewUserView(User user);
        void updateFollowerCount(int count);
        void updateFolloweeCount(int count);
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
    public UserResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        UserService userService = getUserService();
        return userService.getFollowers(request);
    }

    /**
     * Returns the users specified in the request. Uses information in
     * the request object to limit the number of users returned and to return the next set of
     * users after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the users.
     */
    public UserResponse getFollowees(FolloweeRequest request) throws IOException, TweeterRemoteException {
        UserService userService = getUserService();
        return userService.getFollowees(request);
    }

    public GetCountResponse getFolloweeCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getUserService().getFolloweeCount(request);
    }

    public GetCountResponse getFollowerCount(GetCountRequest request) throws IOException, TweeterRemoteException {
        return getUserService().getFollowerCount(request);
    }

    public UserResponse getUser(UserRequest request) throws IOException, TweeterRemoteException {
        return getUserService().getUser(request);
    }

    /**
     * Returns an instance of {@link UserService}. Allows mocking of the UserService class
     * for testing purposes. All usages of UserService should get their UserService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public UserService getUserService() {
        return new UserServiceProxy();
    }
}
