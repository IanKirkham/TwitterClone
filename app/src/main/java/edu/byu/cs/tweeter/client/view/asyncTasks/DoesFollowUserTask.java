package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.client.presenter.FollowEventPresenter;

public class DoesFollowUserTask extends AsyncTask<DoesFollowRequest, Void, DoesFollowResponse> implements FollowEventTask {
    private final FollowEventPresenter presenter;
    private final FollowEventTask.Observer observer;
    private Exception exception;

    /**
     * Creates an instance.
     *
     * @param presenter the presenter this task should use when making an unfollow request.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public DoesFollowUserTask(FollowEventPresenter presenter, UnfollowUserTask.Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on a background thread to handle an unfollow user request. This method is
     * invoked indirectly by calling execute().
     *
     * @param doesFollowRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected DoesFollowResponse doInBackground(DoesFollowRequest... doesFollowRequests) {
        DoesFollowResponse doesFollowResponse = null;

        try {
            doesFollowResponse = presenter.doesFollowUser(doesFollowRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return doesFollowResponse;
    }

    /**
     * Notifies the observer (on the thread of the invoker of the
     * execute method) when the task completes.
     *
     * @param doesFollowResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(DoesFollowResponse doesFollowResponse) {
        if (exception != null) {
            observer.handleException(exception);
        } else if (doesFollowResponse.isSuccess()) {
            observer.requestSuccessful(doesFollowResponse);
        } else {
            observer.requestUnsuccessful(doesFollowResponse);
        }
    }
}
