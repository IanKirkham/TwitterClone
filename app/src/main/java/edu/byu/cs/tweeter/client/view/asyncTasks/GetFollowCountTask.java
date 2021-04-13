package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.response.GetCountResponse;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;

/**
 * An {@link AsyncTask} for retrieving follower/followee counts.
 */
public abstract class GetFollowCountTask extends AsyncTask<GetCountRequest, Void, GetCountResponse> {
    protected final UserPresenter presenter;
    protected final Observer observer;
    protected Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void updateFollowerCount(int count);
        void updateFolloweeCount(int count);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve users.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetFollowCountTask(UserPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve follower/followee counts.
     *
     * @param getCountRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected abstract GetCountResponse doInBackground(GetCountRequest... getCountRequests);

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param getCountResponse the response that was received by the task.
     */
    @Override
    protected abstract void onPostExecute(GetCountResponse getCountResponse);
}
