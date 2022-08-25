package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/** Adapted from UW CSE340 accessibility assignment*/

public abstract class AbstractActivity extends AppCompatActivity {
    /** Interface for accessing and modifying preference data */
    protected SharedPreferences mSharedPreferences;

    /** The context for this activity */
    protected Context mContext;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSharedPreferences = null;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Set up all of the click listeners for the settings and home buttons on the screen.
     */
    protected void setNavigationListeners() {
        View forecast = findViewById(R.id.forecast_page);
        if (forecast != null) {
            forecast.setOnClickListener((view) -> switchActivity(view.getContext(), ForecastActivity.class));
        }
        View personal = findViewById(R.id.personal_page);
        if (personal != null) {
            personal.setOnClickListener((view) -> switchActivity(view.getContext(), PersonalActivity.class));
        }
        View settings = findViewById(R.id.settings_page);
        if (settings != null) {
            settings.setOnClickListener(view -> switchActivity(view.getContext(), SettingsActivity.class));
        }
    }

    /**
     * Switches to the provided view's context to the provided activity class.
     * @param context The context that we are switching from.
     * @param intentClass The activity class to switch to.
     */
    protected void switchActivity(Context context, Class<?> intentClass) {
        Intent settingsIntent = new Intent(context, intentClass);
        context.startActivity(settingsIntent);
    }

    /**
     * Get the shared preferences for this activity/context based on the app package name.
     * First time through it gets this from the system.
     * @return The shared preferences for the application, or null if we were unable to get it.
     */
    protected SharedPreferences getPrefs() {
        if (mSharedPreferences == null) {
            try {
                Context context = getApplicationContext();
                mSharedPreferences = context.getSharedPreferences(
                        context.getPackageName() + ".PREFERENCES",
                        Context.MODE_PRIVATE
                );
            } catch (Exception e) { // Failed to edit shared preferences file
                Toast.makeText(this, "pref error", Toast.LENGTH_LONG).show();
            }
        }
        return mSharedPreferences;
    }

    /**
     * Checks whether the permission has been granted in the given context.
     * @param context The context to check self permissions.
     * @param permission The permissions to check.
     * @return True if the permissions are granted, otherwise false.
     */
    protected boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}