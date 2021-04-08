package edu.byu.cs.tweeter.client.view.main.statuses;


import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;

import edu.byu.cs.tweeter.client.view.asyncTasks.GetStatusesTask;

/**
 * The fragment that displays on the 'Story' tab.
 */
public class FeedFragment extends StatusesFragment {

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static FeedFragment newInstance(User rootUser, User user, AuthToken authToken) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(ROOT_USER_KEY, rootUser);
        args.putSerializable(CURRENT_USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected StatusRecyclerViewAdapter getRecyclerViewAdapter() {
        return new FeedRecyclerViewAdapter();
    }


    private class FeedRecyclerViewAdapter extends StatusRecyclerViewAdapter {
        @Override
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFeedTask getFeedTask = new GetFeedTask(presenter, presenter);
            FeedRequest request = new FeedRequest(user.getAlias(), PAGE_SIZE, lastStatus, authToken);
            getFeedTask.execute(request);
        }
    }
}