package edu.byu.cs.tweeter.view.main.statuses;


import android.os.Bundle;


import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.view.asyncTasks.GetStatusesTask;

/**
 * The fragment that displays on the 'Story' tab.
 */
public class StoryFragment extends StatusesFragment {

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static StoryFragment newInstance(User user, AuthToken authToken) {
        StoryFragment fragment = new StoryFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected StatusRecyclerViewAdapter getRecyclerViewAdapter() {
        return new StoryRecyclerViewAdapter();
    }


    private class StoryRecyclerViewAdapter extends StatusRecyclerViewAdapter {
        @Override
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStatusesTask getStatusesTask = new GetStatusesTask(presenter, this);
            ArrayList<String> retrieveStatusesFor = new ArrayList<>(Arrays.asList(user.getAlias()));
            StatusesRequest request = new StatusesRequest(retrieveStatusesFor, PAGE_SIZE, (lastStatus == null ? null : lastStatus.getTimePublished()));
            getStatusesTask.execute(request);
        }
    }
}