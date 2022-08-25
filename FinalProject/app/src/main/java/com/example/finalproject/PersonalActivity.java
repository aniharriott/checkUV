package com.example.finalproject;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PersonalActivity extends AbstractActivity{

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);
        mContext = PersonalActivity.this;
        setNavigationListeners();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
    }
}
