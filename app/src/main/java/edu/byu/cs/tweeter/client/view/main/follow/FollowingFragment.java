package edu.byu.cs.tweeter.client.view.main.follow;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.view.asyncTasks.GetFolloweesTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FolloweeRequest;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUsersTask;

/**
 * The fragment that displays on the 'Followers' tab.
 */
public class FollowingFragment extends UserDisplayFragment {
    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static FollowingFragment newInstance(User user, AuthToken authToken) {
        FollowingFragment fragment = new FollowingFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected UserDisplayRecyclerViewAdapter getRecyclerViewAdapter() {
        return new FollowingRecyclerViewAdapter();
    }

    /**
     * The adapter for the RecyclerView that displays the Follower data.
     */
    private class FollowingRecyclerViewAdapter extends UserDisplayFragment.UserDisplayRecyclerViewAdapter {
        /**
         * Causes the Adapter to display a loading footer and make a request to get more follower
         * data.
         */
        @Override
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFolloweesTask getFolloweesTask = new GetFolloweesTask(presenter, this);
            FolloweeRequest request = new FolloweeRequest(user.getAlias(), PAGE_SIZE, (lastFollower == null ? null : lastFollower.getAlias()));

            getFolloweesTask.execute(request);
        }
    }
}
