package edu.byu.cs.tweeter.view.main.story;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StatusesRequest;
import edu.byu.cs.tweeter.model.service.response.StatusesResponse;
import edu.byu.cs.tweeter.presenter.StatusesPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetStatusesTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The fragment that displays on the 'Story' tab.
 */
public class StoryFragment extends Fragment implements StatusesPresenter.View { // TODO: Eventually abstract away the necessary parts and use Template Pattern on Story and Feed

    private static final String LOG_TAG = "StoryFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private StatusesPresenter presenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new StatusesPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    /**
     * The ViewHolder for the RecyclerView that displays the Story data.
     */
    private class StatusHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView statusTimePublished;
        private final TextView statusContent;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        StatusHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(viewType == ITEM_VIEW) {
                userImage = itemView.findViewById(R.id.userImage);
                userAlias = itemView.findViewById(R.id.userAlias);
                userName = itemView.findViewById(R.id.userName);
                statusTimePublished = itemView.findViewById(R.id.statusTimePublished);
                statusContent = itemView.findViewById(R.id.statusContent);
            } else {
                userImage = null;
                userAlias = null;
                userName = null;
                statusTimePublished = null;
                statusContent = null;
            }
        }

        /**
         * Binds the status's data to the view.
         *
         * @param status the Status.
         */
        void bindStatus(Status status) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(status.getAuthor().getImageBytes()));

            SpannableString spannableAlias = new SpannableString(status.getAuthor().getAlias());
            spannableAlias.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(getContext(), "Clicked the user alias!", Toast.LENGTH_LONG).show();
                    // TODO: present new MainActivity with the new User
                }
            }, 0, spannableAlias.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            userAlias.setMovementMethod(LinkMovementMethod.getInstance());
            userAlias.setText(spannableAlias);

            userName.setText(status.getAuthor().getName());
            statusTimePublished.setText(status.getTimePublished().toString());

            statusContent.setMovementMethod(LinkMovementMethod.getInstance());
            statusContent.setText(spanMentions(spanURLs(status.getContent())));
        }

        private SpannableString spanURLs(String message) { // TODO: Find a home for orphaned functions
            SpannableString string = new SpannableString(message);
            String regex = "((https?|ftp)://)?www\\.[a-zA-Z0-9]+\\.[a-zA-Z]+";
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(message);

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                string.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        TextView tv = (TextView) widget;
                        Spanned s = (Spanned) tv.getText();
                        int start = s.getSpanStart(this);
                        int end = s.getSpanEnd(this);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.subSequence(start, end).toString())); // TODO: Default protocol to http
                        startActivity(browserIntent);
                    }
                }, start, end, Spanned.SPAN_COMPOSING);
            }

            return string;
        }

        private SpannableString spanMentions(SpannableString message) { // TODO: Find a home for orphaned functions
            SpannableString string = message;
            String regex = "@[a-zA-Z0-9|_|-]+";
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(message);

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                string.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Toast.makeText(getContext(), "Clicked a Mention!", Toast.LENGTH_LONG).show();
                    }
                }, start, end, Spanned.SPAN_COMPOSING);
            }

            return string;
        }

    }

    /**
     * The adapter for the RecyclerView that displays the Story data.
     */
    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StatusHolder> implements GetStatusesTask.Observer {

        private final List<Status> statuses = new ArrayList<>();
        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of story data.
         */
        StoryRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new statuses to the list from which the RecyclerView retrieves the statuses it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newStatuses the users to add.
         */
        void addItems(List<Status> newStatuses) {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        /**
         * Adds a single status to the list from which the RecyclerView retrieves the statuses it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param status the user to add.
         */
        void addItem(Status status) {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param status the status to remove.
         */
        void removeItem(Status status) {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         *  Creates a view holder for a status to be displayed in the RecyclerView or for a message
         *  indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public StatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if (viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StatusHolder(view, viewType);
        }

        /**
         * Binds the status at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         * @param statusHolder the ViewHolder to which the status should be bound.
         * @param position the position (in the list of statuses) that contains the status to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull StatusHolder statusHolder, int position) {
            if(!isLoading) {
                statusHolder.bindStatus(statuses.get(position));
            }
        }

        /**
         * Returns the current number of statuses available for display.
         * @return the number of statuses available for display.
         */
        @Override
        public int getItemCount() {
            return statuses.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more story
         * data.
         */
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStatusesTask getStatusesTask = new GetStatusesTask(presenter, this);
            ArrayList<String> retrieveStatusesFor = new ArrayList<>(Arrays.asList(user.getAlias()));
            StatusesRequest request = new StatusesRequest(retrieveStatusesFor, PAGE_SIZE, (lastStatus == null ? null : lastStatus.getTimePublished()));
            getStatusesTask.execute(request);
        }

        /**
         * A callback indicating more story data has been received. Loads the new statuses
         * and removes the loading footer.
         *
         * @param statusesResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void storyRetrieved(StatusesResponse statusesResponse) {
            List<Status> statuses = statusesResponse.getStatuses();

            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() -1) : null;
            hasMorePages = statusesResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statuses);
        }

        /**
         * A callback indicating that an exception was thrown by the presenter.
         *
         * @param exception the exception.
         */
        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * Adds a dummy status to the list of statuses so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        private void addLoadingFooter() {
            addItem(new Status("Dummy message", new User("Dummy", "User", ""), LocalDateTime.now()));
        }

        /**
         * Removes the dummy status from the list of statuses so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
