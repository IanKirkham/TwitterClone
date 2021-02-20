package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowUserService;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;

public class FollowUserPresenter {

    private final FollowUserPresenter.View view;

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
    public FollowUserPresenter(FollowUserPresenter.View view) {
        this.view = view;
    }

    /**
     * Makes a follow user request.
     *
     * @param followUserRequest the request.
     */
    public FollowUserResponse followUser(FollowUserRequest followUserRequest) throws IOException {
        FollowUserService followUserService = new FollowUserService();
        return followUserService.followUser(followUserRequest);
    }
}
