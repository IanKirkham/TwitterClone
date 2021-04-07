package edu.byu.cs.tweeter.client.view.asyncTasks;

import java.io.IOException;

import edu.byu.cs.tweeter.client.presenter.StatusesPresenter;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

public class GetFeedTask extends GetStatusesTask {
    /**
     * Creates an instance.
     *
     * @param presenter the presenter from whom this task should retrieve stories.
     * @param observer  the observer who wants to be notified when this task completes.
     */
    public GetFeedTask(StatusesPresenter presenter, Observer observer) {
        super(presenter, observer);
    }

    @Override
    protected StatusesResponse doInBackground(StatusesRequest... statusesRequests) {
        StatusesResponse response = null;

        try {
            response = presenter.getFeed((FeedRequest) statusesRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }
}
