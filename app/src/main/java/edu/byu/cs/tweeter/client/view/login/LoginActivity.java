package edu.byu.cs.tweeter.client.view.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edu.byu.cs.tweeter.client.view.login.SectionsPagerAdapter sectionsPagerAdapter = new edu.byu.cs.tweeter.client.view.login.SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.loginViewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.loginTabs);
        tabs.setupWithViewPager(viewPager);
    }
}
