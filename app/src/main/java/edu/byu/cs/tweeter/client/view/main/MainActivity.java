package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.client.presenter.UserPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFolloweesCountTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowersCountTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetCountRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.client.presenter.PostPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.main.post.PostFragment;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements LogoutPresenter.View, UserPresenter.View {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private User user;
    private AuthToken authToken;

    private LogoutPresenter presenter;
    private UserPresenter userPresenter;

    private TextView followeeCountText;
    private TextView followerCountText;
    private int followeeCount = 0;
    private int followerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        presenter = new LogoutPresenter(this);
        userPresenter = new UserPresenter(this);

        GetCountRequest getCountRequest = new GetCountRequest(user.getAlias());
        GetFollowersCountTask getFollowersCount = new GetFollowersCountTask(userPresenter, userPresenter);
        GetFolloweesCountTask getFolloweesCount = new GetFolloweesCountTask(userPresenter, userPresenter);
        getFollowersCount.execute(getCountRequest);
        getFolloweesCount.execute(getCountRequest);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostFragment fragment = PostFragment.newInstance(user, authToken);
                PostPresenter observer = sectionsPagerAdapter.getStoryFragment().getPostPresenter();
                fragment.setTaskObserver(observer);
                fragment.show(getSupportFragmentManager(), "Post");
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        followeeCountText = findViewById(R.id.followeeCount);
        followeeCountText.setText(getString(R.string.followeeCount, followeeCount));

        followerCountText = findViewById(R.id.followerCount);
        followerCountText.setText(getString(R.string.followerCount, followerCount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            Toast.makeText(this, "Logging out", Toast.LENGTH_LONG).show();

            LogoutRequest logoutRequest = new LogoutRequest(user.getAlias(), authToken);
            LogoutTask logoutTask = new LogoutTask(presenter, presenter);
            logoutTask.execute(logoutRequest);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(this, logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(this, "Error: " + exception.getMessage(), Toast.LENGTH_LONG).show();
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