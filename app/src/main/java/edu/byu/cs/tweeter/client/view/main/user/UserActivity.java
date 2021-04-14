package edu.byu.cs.tweeter.client.view.main.user;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.DoesFollowUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFolloweesCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.DoesFollowRequest;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.DoesFollowResponse;
import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;
import edu.byu.cs.tweeter.client.presenter.FollowEventPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.UnfollowUserTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;

public class UserActivity extends AppCompatActivity implements FollowEventPresenter.View, UserPresenter.View {

    private static final String LOG_TAG = "UserActivity";

    public static final String ROOT_USER_KEY = "RootUser";
    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowEventPresenter followEventPresenter;
    private UserPresenter userPresenter;

    private ToggleButton followButton;
    private TextView followeeCountText;
    private TextView followerCountText;
    private int followeeCount = 0;
    private int followerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        User rootUser = (User) getIntent().getSerializableExtra(ROOT_USER_KEY);
        if (rootUser == null) {
            throw new RuntimeException("Root user not passed to activity");
        }

        User currentUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(currentUser == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        followEventPresenter = new FollowEventPresenter(this);
        userPresenter = new UserPresenter(this);

        GetCountRequest getCountRequest = new GetCountRequest(currentUser.getAlias());
        GetFollowersCountTask getFollowersCount = new GetFollowersCountTask(userPresenter, userPresenter);
        GetFolloweesCountTask getFolloweesCount = new GetFolloweesCountTask(userPresenter, userPresenter);
        getFollowersCount.execute(getCountRequest);
        getFolloweesCount.execute(getCountRequest);

        if (!rootUser.equals(currentUser)) {
            DoesFollowUserTask doesFollowUserTask = new DoesFollowUserTask(followEventPresenter, followEventPresenter);
            DoesFollowRequest doesFollowRequest = new DoesFollowRequest(rootUser.getAlias(), authToken , currentUser.getAlias(), rootUser.getName(), currentUser.getName());
            doesFollowUserTask.execute(doesFollowRequest);
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), rootUser, currentUser, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        TextView userName = findViewById(R.id.userName);
        userName.setText(currentUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(currentUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(currentUser.getImageBytes()));

        followeeCountText = findViewById(R.id.followeeCount);
        followeeCountText.setText(getString(R.string.followeeCount, followeeCount));

        followerCountText = findViewById(R.id.followerCount);
        followerCountText.setText(getString(R.string.followerCount, followerCount));

        followButton = findViewById(R.id.followButton);
        followButton.setChecked(false); //Assume they don't follow until proven otherwise with the DoesFollowUserTask
        followButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateFollowerCount(followerCount + 1);
                    FollowUserRequest followUserRequest = new FollowUserRequest(rootUser.getAlias(), authToken , currentUser.getAlias(), rootUser.getName(), currentUser.getName());
                    FollowUserTask followUserTask = new FollowUserTask(followEventPresenter, followEventPresenter);
                    followUserTask.execute(followUserRequest);
                } else {
                    updateFollowerCount(followerCount - 1);
                    UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(rootUser.getAlias(), authToken , currentUser.getAlias(), rootUser.getName(), currentUser.getName());
                    UnfollowUserTask unfollowUserTask = new UnfollowUserTask(followEventPresenter, followEventPresenter);
                    unfollowUserTask.execute(unfollowUserRequest);
                }
            }
        });

        if (rootUser.equals(currentUser)) {
            followButton.setEnabled(false);
            followButton.setText("Same User");
        }
    }

    @Override
    public void requestSuccessful(FollowEventResponse response) {
        if (response instanceof DoesFollowResponse) {
            followButton.setChecked(response.isSuccess());
        }
    }

    @Override
    public void requestUnsuccessful(FollowEventResponse response) {
        if (response instanceof DoesFollowResponse) {
            followButton.setChecked(response.isSuccess());
            return;
        } else if (response instanceof FollowUserResponse) {
            updateFollowerCount(followerCount - 1);
        } else if (response instanceof UnfollowUserResponse) {
            updateFollowerCount(followerCount + 1);
        }

        followButton.setChecked(!followButton.isChecked());
    }

    @Override
    public void handleException(Exception exception) {
        followButton.setChecked(!followButton.isChecked());
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Error: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        if (exception.getMessage() != null && exception.getMessage().contains("Authentication")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void presentNewUserView(User user) {}

    @Override
    public void updateFollowerCount(int count) {
        followerCount = count;
        followerCountText.setText(getString(R.string.followerCount, followerCount));
    }

    @Override
    public void updateFolloweeCount(int count) {
        followeeCount = count;
        followeeCountText.setText(getString(R.string.followeeCount, followeeCount));
    }
}