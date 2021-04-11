package edu.byu.cs.tweeter.client.view.main.post;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.PostRequest;
import edu.byu.cs.tweeter.model.service.response.PostResponse;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.PostTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * The fragment that displays on the 'Following' tab.
 */
    public class PostFragment extends DialogFragment implements PostPresenter.View {

    private static final String LOG_TAG = "PostFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private User user;
    private AuthToken authToken;
    private PostPresenter presenter;
    private PostTask.Observer observer;

    private TextView userName;
    private TextView userAlias;
    private EditText postContent;
    private Button postButton;
    private ImageButton closeButton;
    private ImageView userImage;


    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @param user the logged in user.
     * @param authToken the auth token for this user's session.
     * @return the fragment.
     */
    public static PostFragment newInstance(User user, AuthToken authToken) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new PostPresenter(this);

        bindViews(view);

        return view;
    }

    private void bindViews(View view) {
        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment.this.dismiss();
            }
        });

        postContent = view.findViewById(R.id.statusText);
        postContent.setHint(getString(R.string.postPrompt));

        postButton = view.findViewById(R.id.postButton);
        postButton.setText(getString(R.string.post));
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = postContent.getText().toString();

                PostRequest request = new PostRequest(user.getAlias(), postText, LocalDateTime.now(), authToken);
                PostTask task = new PostTask(presenter, PostFragment.this.observer);
                task.execute(request);

                Toast.makeText(getContext(), "Saving Post!", Toast.LENGTH_SHORT).show();

                PostFragment.this.dismiss();
            }
        });

        userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());

        userAlias = view.findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        userImage = view.findViewById(R.id.userImage);
        userImage.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
    }

    public void setTaskObserver(PostTask.Observer observer) {
        this.observer = observer;
    }

    @Override
    public void postSaved(PostResponse postResponse) {
        Toast.makeText(getContext(), "Post saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(getContext(), "Error: " + exception.getMessage() , Toast.LENGTH_SHORT).show();
        if (exception.getMessage() != null && exception.getMessage().contains("Authentication")) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
