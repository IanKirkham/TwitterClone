package edu.byu.cs.tweeter.client.view.asyncTasks;

import java.io.IOException;

import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;

public class GetFollowersTask extends GetUsersTask {
    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve users.
     * @param observer  the observer who wants to be notified when this task completes.
     */
    public GetFollowersTask(UserPresenter presenter, Observer observer) {
        super(presenter, observer);
    }

    @Override
    protected UserResponse doInBackground(UserRequest... userRequests) {
        UserResponse response = null;

        try {
            response = presenter.getFollowers((FollowerRequest) userRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }
}
