package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.model.service.response.UserResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;


/**
 * An {@link AsyncTask} for retrieving users.
 */
public class GetUsersTask extends AsyncTask<UserRequest, Void, UserResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void usersRetrieved(UserResponse userResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve users.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetUsersTask(UserPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followers. This method is
     * invoked indirectly by calling {@link #execute(UserRequest...)}.
     *
     * @param userRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected UserResponse doInBackground(UserRequest... userRequests) {

        UserResponse response = null;

        try {
            response = presenter.getUsers(userRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param userResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(UserResponse userResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.usersRetrieved(userResponse);
        }
    }
}
