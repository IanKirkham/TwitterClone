package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.view.main.followers.FollowerFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.main.statuses.FeedFragment;
import edu.byu.cs.tweeter.view.main.statuses.StoryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the sections/tabs/pages
 * of the Main Activity.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int FEED_FRAGMENT_POSITION = 0;
    private static final int STORY_FRAGMENT_POSITION = 1;
    private static final int FOLLOWING_FRAGMENT_POSITION = 2;
    private static final int FOLLOWER_FRAGMENT_POSITION = 3;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.feedTabTitle, R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;
    private final User user;
    private final AuthToken authToken;

    private final Fragment[] fragments = new Fragment[4];

    public SectionsPagerAdapter(Context context, FragmentManager fm, User user, AuthToken authToken) {
        super(fm);
        mContext = context;
        this.user = user;
        this.authToken = authToken;
    }

    @Override
    public Fragment getItem(int position) { // TODO: add newly implemented fragments here
        switch (position) {
            case FOLLOWING_FRAGMENT_POSITION:
                return FollowingFragment.newInstance(user, authToken);
            case  STORY_FRAGMENT_POSITION:
                return StoryFragment.newInstance(user, authToken);
            case FEED_FRAGMENT_POSITION:
                return FeedFragment.newInstance(user, authToken);
            case FOLLOWER_FRAGMENT_POSITION:
                return FollowerFragment.newInstance(user, authToken);
            default:
                return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        fragments[position] = createdFragment;

        return createdFragment;
    }

    public StoryFragment getStoryFragment() {
        if (fragments[STORY_FRAGMENT_POSITION] == null) { return null; }
        if (!(fragments[STORY_FRAGMENT_POSITION] instanceof StoryFragment)) { return null; }

        return (StoryFragment) fragments[STORY_FRAGMENT_POSITION];
    }
}