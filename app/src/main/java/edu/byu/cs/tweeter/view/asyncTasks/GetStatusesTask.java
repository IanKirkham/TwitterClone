package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.presenter.StatusesPresenter;

/**
 * An {@link AsyncTask} for retrieving statuses for a user.
 */
public class GetStatusesTask extends AsyncTask<StatusesRequest, Void, StatusesResponse> {

    private final StatusesPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void storyRetrieved(StatusesResponse statusesResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve stories.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public GetStatusesTask(StatusesPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to retrieve followees. This method is
     * invoked indirectly by calling {@link #execute(StatusesRequest...)}.
     *
     * @param statusesRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected StatusesResponse doInBackground(StatusesRequest... statusesRequests) {

        StatusesResponse response = null;

        try {
            response = presenter.getStory(statusesRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param statusesResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(StatusesResponse statusesResponse) {
        if (exception != null) {
            observer.handleException(exception);
        } else {
            observer.storyRetrieved(statusesResponse);
        }
    }
}
