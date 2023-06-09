package com.example.goaled;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavBar;

    private UserAccomplishFragment accomplishFragment;
    private UserActivitiesFragment activitiesFragment;
    private UserGoalsFragment goalsFragment;
    private UserProfileFragment profileFragment;
    private UserStatsFragment statsFragment;

    private UserLocal userLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Intent intent = getIntent();
        userLocal = (UserLocal) intent.getSerializableExtra("USER");
        Log.d("uygar", userLocal.getEmail());
        profileFragment = UserProfileFragment.newInstance(userLocal);

        bottomNavBar = findViewById(R.id.bottom_navigation);
        bottomNavBar.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
        bottomNavBar.setSelectedItemId(R.id.profile);


        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.accomplish:
                        accomplishFragment = UserAccomplishFragment.newInstance(userLocal);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accomplishFragment).commit();
                        return true;
                    case R.id.activities:
                        activitiesFragment = UserActivitiesFragment.newInstance(userLocal);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, activitiesFragment).commit();
                        return true;
                    case R.id.goals:
                        goalsFragment = UserGoalsFragment.newInstance(userLocal);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, goalsFragment).commit();
                        return true;
                    case R.id.profile:
                        profileFragment = UserProfileFragment.newInstance(userLocal);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;
                    case R.id.stats:
                        statsFragment = UserStatsFragment.newInstance(userLocal);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, statsFragment).commit();
                        return true;
                }
                return false;
            }
        });


    }

    public UserLocal getUserLocal() {
        return userLocal;
    }
}