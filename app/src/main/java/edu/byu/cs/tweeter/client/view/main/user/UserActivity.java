package edu.byu.cs.tweeter.client.view.main.user;

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
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowEventResponse;
import edu.byu.cs.tweeter.client.presenter.FollowEventPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowUserTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.UnfollowUserTask;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity implements FollowEventPresenter.View {

    private static final String LOG_TAG = "UserActivity";

    public static final String ROOT_USER_KEY = "RootUser";
    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowEventPresenter presenter;

    private ToggleButton followButton;

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

        presenter = new FollowEventPresenter(this);

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

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 42));

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 27));

        followButton = findViewById(R.id.followButton);
        if (rootUser.getFollowees().contains(currentUser.getAlias())) {
            followButton.setChecked(true);
        }
        followButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FollowUserRequest followUserRequest = new FollowUserRequest(rootUser, new AuthToken(), currentUser);
                    FollowUserTask followUserTask = new FollowUserTask(presenter, presenter);
                    followUserTask.execute(followUserRequest);
                } else {
                    UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(rootUser, new AuthToken(), currentUser);
                    UnfollowUserTask unfollowUserTask = new UnfollowUserTask(presenter, presenter);
                    unfollowUserTask.execute(unfollowUserRequest);
                }
            }
        });
    }

    @Override
    public void requestSuccessful(FollowEventResponse response) {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void requestUnsuccessful(FollowEventResponse response) {
        followButton.setChecked(!followButton.isChecked());
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception exception) {
        followButton.setChecked(!followButton.isChecked());
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to unfollow user because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}