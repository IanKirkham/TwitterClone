package edu.byu.cs.tweeter.view.main.user;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowUserRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.UnfollowUserRequest;
import edu.byu.cs.tweeter.model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.presenter.FollowUserPresenter;
import edu.byu.cs.tweeter.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.presenter.UnfollowUserPresenter;
import edu.byu.cs.tweeter.util.ByteArrayUtils;
import edu.byu.cs.tweeter.view.asyncTasks.FollowUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoginTask;
import edu.byu.cs.tweeter.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.view.asyncTasks.UnfollowUserTask;
import edu.byu.cs.tweeter.view.login.LoginFragment;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity implements FollowUserPresenter.View, FollowUserTask.Observer, UnfollowUserPresenter.View, UnfollowUserTask.Observer {

    private static final String LOG_TAG = "UserActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private FollowUserPresenter followUserPresenter;
    private UnfollowUserPresenter unfollowUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        User user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        followUserPresenter = new FollowUserPresenter(this);        // TODO: we probably only need one presenter for this page, so combine the two?
        unfollowUserPresenter = new UnfollowUserPresenter(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        if (user.getImageBytes() != null) { // right now it breaks without this check (because we have some fake users in the "mentions" of a status without an imageByte array
            userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));
        }

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 42));

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 27));

        ToggleButton followButton = findViewById(R.id.followButton);
        followButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    User dummyUser = new User("first", "last", "url");
                    FollowUserRequest followUserRequest = new FollowUserRequest(dummyUser, new AuthToken(), dummyUser);
                    FollowUserTask followUserTask = new FollowUserTask(followUserPresenter, UserActivity.this);
                    followUserTask.execute(followUserRequest);
                } else {
                    User dummyUser = new User("first", "last", "url");
                    UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(dummyUser, new AuthToken(), dummyUser);
                    UnfollowUserTask unfollowUserTask = new UnfollowUserTask(unfollowUserPresenter, UserActivity.this);
                    unfollowUserTask.execute(unfollowUserRequest);
                }
            }
        });
    }

    @Override
    public void followSuccessful(FollowUserResponse followUserResponse) {
        Toast.makeText(this, followUserResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void followUnsuccessful(FollowUserResponse followUserResponse) {
        Toast.makeText(this, followUserResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleFollowException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to follow user because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void unfollowSuccessful(UnfollowUserResponse unfollowUserResponse) {
        Toast.makeText(this, unfollowUserResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void unfollowUnsuccessful(UnfollowUserResponse unfollowUserResponse) {
        Toast.makeText(this, unfollowUserResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleUnfollowException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Failed to unfollow user because of exception: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}