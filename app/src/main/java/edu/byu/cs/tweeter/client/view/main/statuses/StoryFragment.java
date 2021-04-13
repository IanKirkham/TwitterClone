package edu.byu.cs.tweeter.client.view.main.statuses;


import android.os.Bundle;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import edu.byu.cs.tweeter.client.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetStatusesTask;

/**
 * The fragment that displays on the 'Story' tab.
 */
public class StoryFragment extends StatusesFragment implements PostPresenter.View {
    private PostPresenter postPresenter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static StoryFragment newInstance(User rootUser, User user, AuthToken authToken) {
        StoryFragment fragment = new StoryFragment();

        fragment.postPresenter = new PostPresenter(fragment);

        Bundle args = new Bundle(2);
        args.putSerializable(ROOT_USER_KEY, rootUser);
        args.putSerializable(CURRENT_USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected StatusRecyclerViewAdapter getRecyclerViewAdapter() {
        return new StoryRecyclerViewAdapter();
    }

    public PostPresenter getPostPresenter() {
        return this.postPresenter;
    }

    @Override
    public void postSaved(PostResponse postResponse) {
        if (postResponse.getStatus().getAuthor().getAlias().equals(this.rootUser.getAlias())) {
            postResponse.getStatus().setAuthor(this.rootUser);
        } else if (postResponse.getStatus().getAuthor().getAlias().equals(this.user.getAlias())) {
            postResponse.getStatus().setAuthor(this.user);
        }
        statusRecyclerViewAdapter.statuses.add(0, postResponse.getStatus());
        statusRecyclerViewAdapter.notifyItemInserted(0);
        recyclerLayoutManager.smoothScrollToPosition(statusRecyclerView,null,0);
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(getContext(), "Post Failed to Save", Toast.LENGTH_SHORT).show();
    }

    private class StoryRecyclerViewAdapter extends StatusRecyclerViewAdapter {
        @Override
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(presenter, presenter);
            StoryRequest request = new StoryRequest(user.getAlias(), PAGE_SIZE, lastStatus, authToken);
            getStoryTask.execute(request);
        }
    }
}