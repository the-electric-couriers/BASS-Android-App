package com.electriccouriers.bass.activities;

import android.os.Bundle;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;

public class HomeActivity extends BaseActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, Globals.MAPBOX_API_KEY);

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(Style.LIGHT);
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(51.541519, 4.457777))
                    .zoom(13)
                    .build());
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
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
