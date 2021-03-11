package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;

/**
 * An {@link AsyncTask} for saving a post for a user.
 */
public class PostTask extends AsyncTask<PostRequest, Void, PostResponse> {

    private final PostPresenter presenter;
    private final Observer observer;
    private Exception exception;

    /**
     * An observer interface to be implemented by observers who want to be notified when this task
     * completes.
     */
    public interface Observer {
        void postSaved(PostResponse postResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should save the post.
     * @param observer the observer who wants to be notified when this task completes.
     */
    public PostTask(PostPresenter presenter, Observer observer) {
        this.presenter = presenter;
        this.observer = observer;
    }

    /**
     * The method that is invoked on the background thread to save the post. This method is
     * invoked indirectly by calling {@link #execute(PostRequest...)}.
     *
     * @param postRequests the request object (there will only be one).
     * @return the response.
     */
    @Override
    protected PostResponse doInBackground(PostRequest... postRequests) {

        PostResponse response = null;

        try {
            response = presenter.savePost(postRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    /**
     * Notifies the observer (on the UI thread) when the task completes.
     *
     * @param postResponse the response that was received by the task.
     */
    @Override
    protected void onPostExecute(PostResponse postResponse) {
        if (observer == null) { return; }
        if (exception != null) {
            observer.handleException(exception);
        } else {
            observer.postSaved(postResponse);
        }
    }
}
