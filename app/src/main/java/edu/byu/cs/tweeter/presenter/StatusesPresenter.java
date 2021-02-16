package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.StatusesService;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;

/**
 * The presenter for Feed/Story functionality of the application.
 */
public class StatusesPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
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
    public StatusesResponse getStory(StatusesRequest request) throws IOException {
        StatusesService StatusesService = getStoryService();
        return StatusesService.getStatuses(request);
    }

    /**
     * Returns an instance of {@link StatusesService}. Allows mocking of the StoryService class
     * for testing purposes. All usages of StoryService should get their StoryService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StatusesService getStoryService() {
        return new StatusesService();
    }
}
