package com.electriccouriers.bass.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.electriccouriers.bass.R;

public class RequestActivity extends BaseActivity {

    Button beginLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request);

        beginLocationButton = (Button) findViewById(R.id.beginlocation);
        beginLocationButton.setOnClickListener(v -> {
            Log.e("log", "click");
            PopupMenu pm = new PopupMenu(RequestActivity.this, beginLocationButton);
            pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());
            pm.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.first:
                        Toast.makeText(RequestActivity.this, "Geselecteerd!", Toast.LENGTH_SHORT).show();
                        beginLocationButton.setText("Roosendaal Station");
                        return true;

                    case R.id.second:
                        Toast.makeText(RequestActivity.this, "Geselecteerd!", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.third:
                        Toast.makeText(RequestActivity.this, "Geselecteerd!", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return true;
            });
            pm.show();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_request;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.login_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_back_white;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }
}