package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.presenter.FollowUserPresenter;

public class FollowUserTask extends AsyncTask<FollowUserRequest, Void, FollowUserResponse> {

    private final FollowUserPresenter presenter;
    private final FollowUserTask.Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void followSuccessful(FollowUserResponse followUserResponse);
        void followUnsuccessful(FollowUserResponse followUserResponse);
        void handleFollowException(Exception ex);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter this task should use when making a follow request.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public FollowUserTask(FollowUserPresenter presenter, FollowUserTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on a background thread to handle a follow user request. This method is
     * invoked indirectly by calling {@link #execute(FollowUserRequest...)}.
     *
     * @param followRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected FollowUserResponse doInBackground(FollowUserRequest... followRequests) {
        FollowUserResponse followUserResponse = null;

        try {
            followUserResponse = presenter.followUser(followRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return followUserResponse;
    }

    /**
     * Notifies the observer (on the thread of the invoker of the
     * {@link #execute(FollowUserRequest...)} method) when the task completes.
     *
     * @param followUserResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(FollowUserResponse followUserResponse) {
        if(exception != null) {
            observer.handleFollowException(exception);
        } else if(followUserResponse.isSuccess()) {
            observer.followSuccessful(followUserResponse);
        } else {
            observer.followUnsuccessful(followUserResponse);
        }
    }
}
