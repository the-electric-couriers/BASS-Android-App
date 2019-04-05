package com.electriccouriers.bass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.google.android.material.navigation.NavigationView;
import com.electriccouriers.bass.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Created by Thomas Hopstaken on 16/01/17
 */

/**
 *  HELLO
 */

public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;

    private DrawerLayout menuDrawerLayout;

    /**
     * OnCreate override to init Activity
     * @param savedInstanceState bundle of information of the activity it started in
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        toolbar = findViewById(R.id.toolbar);
        menuDrawerLayout = findViewById(R.id.drawer_layout);

        if(toolbar != null) {
            toolbar.setTitle(getToolbarTitle());
            toolbar.setNavigationIcon(getToolbarNavigationIcon());

            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(this);
        }

        if(menuDrawerLayout != null) {
            // Menu click listeners block
            findViewById(R.id.menu_home_item).setOnClickListener(v -> onMenuItemSelected(R.id.menu_home_item));
            findViewById(R.id.menu_profile_item).setOnClickListener(v -> onMenuItemSelected(R.id.menu_profile_item));
            findViewById(R.id.menu_logout_item).setOnClickListener(v -> onMenuItemSelected(R.id.menu_logout_item));
        }
    }

    /**
     * Gets the Layout Resource ID
     * Example:
     * R.layout.activity_main
     */
    protected abstract int getLayoutResourceId();

    /**
     * Gets the Toolbar Title String
     * Example:
     * R.string.home_title
     */
    protected abstract int getToolbarTitle();

    /**
     * Gets the Toolbar Navigation Icon
     * Example:
     * R.drawable.ic_menu_back
     */
    protected abstract int getToolbarNavigationIcon();

    /**
     * Gets the Toolbar Options Menu
     * Example:
     * R.menu.menu_home
     */
    protected abstract int getOptionsMenu();


    /**
     * OnBackPressed override back button
     */
    @Override
    public void onBackPressed() {
        if(menuDrawerLayout != null && menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START, true);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    /**
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if(getOptionsMenu() != 0)
            getMenuInflater().inflate(getOptionsMenu(), menu);

        return true;
    }

    /**
     * Toolbar click listener override
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(getToolbarNavigationIcon()) {
            case R.drawable.ic_menu:
                openMenu();
                break;
            default:
                onBackPressed();
                break;
        }
    }

    /**
     * @param item
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {

        }

        openMenu();
        return true;
    }


    /**
     * Open given activity
     * @param intent intent
     */
    public void openAcitivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    /**
     * Open the given activity
     * @param intent
     * @param animated
     */
    public void openAcitivity(Intent intent, boolean animated) {
        startActivity(intent);

        if(animated)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showError(String error) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Open the menu drawer
     */
    private void onMenuItemSelected(int menuItem) {
        switch (menuItem) {
            case R.id.menu_logout_item:
                logout();
                break;
            case R.id.menu_profile_item:
                openAcitivity(new Intent(this, ProfileActivity.class), true);
                break;
            default:
                break;
        }

        openMenu();
    }

    /**
     * Open the menu drawer
     */
    private void openMenu() {
        if(menuDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuDrawerLayout.closeDrawer(GravityCompat.START, true);
        } else {
            menuDrawerLayout.openDrawer(GravityCompat.START, true);
        }
    }

    /**
     * Logout current user
     */
    private void logout() {
        PreferenceHelper.clearAll(this);
        openAcitivity(new Intent(BaseActivity.this, StartActivity.class));
        finish();
    }
}
