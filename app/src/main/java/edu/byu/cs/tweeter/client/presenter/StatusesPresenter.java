package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StatusesServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetStatusesTask;

/**
 * The presenter for Feed/Story functionality of the application.
 */
public class StatusesPresenter implements GetStatusesTask.Observer {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void statusesRetrieved(StatusesResponse statusesResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public StatusesPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns the story for the given user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses that form a story.
     */
    public StatusesResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StatusesService statusesService = getStatusesService();
        return statusesService.getStory(request);
    }

    /**
     * Returns the story for the given user. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses that form a story.
     */
    public StatusesResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        StatusesService statusesService = getStatusesService();
        return statusesService.getFeed(request);
    }

    @Override
    public void statusesRetrieved(StatusesResponse statusesResponse) {
        if (view != null) {
            view.statusesRetrieved(statusesResponse);
        }
    }

    @Override
    public void handleException(Exception exception) {
        if (view != null) {
            view.handleException(exception);
        }
    }

    /**
     * Returns an instance of {@link StatusesService}. Allows mocking of the StoryService class
     * for testing purposes. All usages of StoryService should get their StoryService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    public StatusesService getStatusesService() {
        return new StatusesServiceProxy();
    }
}
