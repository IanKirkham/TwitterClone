package edu.byu.cs.tweeter.view.main.follow;

import android.os.Bundle;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.UserRequest;
import edu.byu.cs.tweeter.view.asyncTasks.GetUsersTask;

/**
 * The fragment that displays on the 'Followers' tab.
 */
public class FollowerFragment extends UserDisplayFragment {
    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static FollowerFragment newInstance(User user, AuthToken authToken) {
        FollowerFragment fragment = new FollowerFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected UserDisplayRecyclerViewAdapter getRecyclerViewAdapter() {
        return new FollowerRecyclerViewAdapter();
    }

    /**
     * The adapter for the RecyclerView that displays the Follower data.
     */
    private class FollowerRecyclerViewAdapter extends UserDisplayFragment.UserDisplayRecyclerViewAdapter {
        /**
         * Causes the Adapter to display a loading footer and make a request to get more follower
         * data.
         */
        @Override
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetUsersTask getUsersTask = new GetUsersTask(presenter, this);
            UserRequest request = new UserRequest(user.getFollowers(), PAGE_SIZE, (lastFollower == null ? null : lastFollower.getAlias()));

            getUsersTask.execute(request);
        }
    }
}
