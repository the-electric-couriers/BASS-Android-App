package com.electriccouriers.bass.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.electriccouriers.bass.R;

import androidx.annotation.NonNull;

public class MoreInfoActivity extends BaseActivity{

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

}

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_moreinfo;
    }

    @Override
    protected int getToolbarTitle() {
        return 0;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return 0;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

}
