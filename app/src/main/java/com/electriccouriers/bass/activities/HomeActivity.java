package com.electriccouriers.bass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        findViewById(R.id.buttonAanvragen).setOnClickListener(v -> {
            onClickAanvragen();
        });

        findViewById(R.id.buttonKaart).setOnClickListener(v -> {
            onClickKaart();
        });

        // TEMP DEBUG CODE
        API.service().routePoints(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN)).enqueue(new Callback<List<RoutePoint>>() {
            @Override
            public void onResponse(Call<List<RoutePoint>> call, Response<List<RoutePoint>> response) {
                if(response.isSuccessful()) {
                    System.out.println(response.body().size());
                } else {
                    Log.e("ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<RoutePoint>> call, Throwable t) {
                Log.e("ERROR", t.getLocalizedMessage());
            }
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

    public void onClickAanvragen(){
        //TODO: onClickAanvragen knop functies toevoegen.
    }

    private void onClickKaart() {
        //TODO: onClickKaart knop functies toevoegen.
        openAcitivity(new Intent(HomeActivity.this, CardActivity.class), true);
    }

}
