package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.client.presenter.FollowEventPresenter;

public class UnfollowUserTask extends AsyncTask<UnfollowUserRequest, Void, UnfollowUserResponse> implements FollowEventTask {

    private final FollowEventPresenter presenter;
    private final FollowEventTask.Observer observer;
    private Exception exception;

    /**
     * Creates an instance.
     *
     * @param presenter the presenter this task should use when making an unfollow request.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public UnfollowUserTask(FollowEventPresenter presenter, UnfollowUserTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on a background thread to handle an unfollow user request. This method is
     * invoked indirectly by calling {@link #execute(UnfollowUserRequest...)}.
     *
     * @param unfollowRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected UnfollowUserResponse doInBackground(UnfollowUserRequest... unfollowRequests) {
        UnfollowUserResponse unfollowUserResponse = null;

        try {
            unfollowUserResponse = presenter.unfollowUser(unfollowRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return unfollowUserResponse;
    }

    /**
     * Notifies the observer (on the thread of the invoker of the
     * {@link #execute(UnfollowUserRequest...)} method) when the task completes.
     *
     * @param unfollowUserResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(UnfollowUserResponse unfollowUserResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else if(unfollowUserResponse.isSuccess()) {
            observer.requestSuccessful(unfollowUserResponse);
        } else {
            observer.requestUnsuccessful(unfollowUserResponse);
        }
    }

}
