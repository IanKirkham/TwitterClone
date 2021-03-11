package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.PostServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostService;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.view.asyncTasks.PostTask;

/**
 * The presenter for the post functionality of the application.
 */
public class PostPresenter implements PostTask.Observer {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void postSaved(PostResponse postResponse);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public PostPresenter(View view) {
        this.view = view;
    }

    /**
     * Returns confirmation of saved post
     *
     * @param request contains the data required to fulfill the request.
     * @return the statuses that form a story.
     */
    public PostResponse savePost(PostRequest request) throws IOException, TweeterRemoteException {
        PostService postService = getPostService();
        return postService.savePost(request);
    }

    @Override
    public void postSaved(PostResponse postResponse) {
        if (this.view != null) {
            view.postSaved(postResponse);
        }
    }

    @Override
    public void handleException(Exception exception) {
        if (this.view != null) {
            view.handleException(exception);
        }
    }

    /**
     * Returns an instance of {@link PostService}. Allows mocking of the PostService class
     * for testing purposes. All usages of PostService should get their PostService
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    PostService getPostService() {
        return new PostServiceProxy();
    }
}
