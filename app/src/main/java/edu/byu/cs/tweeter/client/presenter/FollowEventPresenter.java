package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowEventServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowEventService;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowEventTask;

public class FollowEventPresenter implements FollowEventTask.Observer {

    private final FollowEventPresenter.View view;

    @Override
    public void requestSuccessful(FollowEventResponse response) {
        if (view != null) {
            view.requestSuccessful(response);
        }
    }

    @Override
    public void requestUnsuccessful(FollowEventResponse response) {
        if (view != null) {
            view.requestUnsuccessful(response);
        }
    }

    @Override
    public void handleException(Exception exception) {
        if (view != null) {
            view.handleException(exception);
        }
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        public void requestSuccessful(FollowEventResponse response);
        public void requestUnsuccessful(FollowEventResponse response);
        public void handleException(Exception exception);
    }

    /**
     * Makes a follow user request.
     *
     * @param followUserRequest the request.
     */
    public FollowUserResponse followUser(FollowUserRequest followUserRequest) throws IOException, TweeterRemoteException {
        FollowEventService followEventService = getFollowEventService();
        return followEventService.followUser(followUserRequest);
    }

    /**
     * Makes an unfollow user request.
     *
     * @param unfollowUserRequest the request.
     */
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest unfollowUserRequest) throws IOException, TweeterRemoteException {
        FollowEventService followEventService = getFollowEventService();
        return followEventService.unfollowUser(unfollowUserRequest);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public FollowEventPresenter(FollowEventPresenter.View view) {
        this.view = view;
    }

    /**
     * Returns an instance of {@link FollowEventService}. Allows mocking of the FollowEventService class
     * for testing purposes. All usages of FollowEventService should get their FollowEventService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public FollowEventService getFollowEventService() {
        return new FollowEventServiceProxy();
    }
}
