package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;
import edu.byu.cs.tweeter.presenter.FollowingPresenter;

/**
 * An {@link AsyncTask} for retrieving followers for a user.
 */
public class GetFollowersTask extends AsyncTask<FollowerRequest, Void, FollowerResponse> {

    private final FollowerPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void followersRetrieved(FollowerResponse followerResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve followers.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowersTask(FollowerPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(FollowerRequest...)}.
     *
     * @param followerRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowerResponse doInBackground(FollowerRequest... followerRequests) {

        FollowerResponse response = null;

        try {
            response = presenter.getFollowers(followerRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param followerResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowerResponse followerResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.followersRetrieved(followerResponse);
        }
    }
}
