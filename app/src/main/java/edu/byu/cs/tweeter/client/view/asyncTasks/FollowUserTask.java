package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.client.presenter.FollowEventPresenter;

public class FollowUserTask extends AsyncTask<FollowUserRequest, Void, FollowUserResponse> implements FollowEventTask {

    private final FollowEventPresenter presenter;
    private final FollowEventTask.Observer observer;
    private Exception exception;

    /**
     * Creates an instance.
     *
     * @param presenter the presenter this task should use when making a follow request.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public FollowUserTask(FollowEventPresenter presenter, FollowUserTask.Observer observer) {
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
        } catch (IOException | TweeterRemoteException ex) {
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
            observer.handleException(exception);
        } else if(followUserResponse.isSuccess()) {
            observer.requestSuccessful(followUserResponse);
        } else {
            observer.requestUnsuccessful(followUserResponse);
        }
    }
}
