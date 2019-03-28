package com.electriccouriers.bass.activities;

import android.os.Bundle;

import com.electriccouriers.bass.R;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.profile_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_back_black;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }
}
