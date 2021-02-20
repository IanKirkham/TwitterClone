package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UnfollowUserService;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class UnfollowUserPresenter {

    private final UnfollowUserPresenter.View view;

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
    public UnfollowUserPresenter(UnfollowUserPresenter.View view) {
        this.view = view;
    }

    /**
     * Makes an unfollow user request.
     *
     * @param unfollowUserRequest the request.
     */
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest unfollowUserRequest) throws IOException {
        UnfollowUserService unfollowUserService = new UnfollowUserService();
        return unfollowUserService.unfollowUser(unfollowUserRequest);
    }
}
