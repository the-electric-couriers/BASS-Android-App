package com.electriccouriers.bass.activities;

import android.os.Bundle;

import com.electriccouriers.bass.R;

public class KaartActivity extends BaseActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_kaart;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.home_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_menu;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }


}
