package com.electriccouriers.bass.activities;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.fragments.CheckInSheetDialogFragment;
import com.electriccouriers.bass.interfaces.CheckInDialogCloseListener;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

public class HomeActivity extends BaseActivity implements CheckInDialogCloseListener {

    private static final String DOT_SOURCE_ID = "ic_shuttle";

    private LinearLayout requestButtonLayout, cardButtonLayout, currentRideButtonLayout, checkInButtonLayout;
    private TextView currentRideButtonTitle, checkInButtonTitle;

    private int count = 0;
    private double mapLat = 51.541519;
    private double mapLong = 4.457777;
    private double mapZoom = 13.3;
    private long shuttleSpeed = 1000;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Handler handler;
    private Runnable runnable;
    private GeoJsonSource dotGeoJsonSource;
    private ValueAnimator markerIconAnimator;
    private LatLng markerIconCurrentLocation;
    private List<Point> routeCoordinateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, Globals.MAPBOX_API_KEY);

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapView);
        requestButtonLayout = findViewById(R.id.layout_home_request);
        currentRideButtonLayout = findViewById(R.id.layout_home_current_ride);
        currentRideButtonTitle = findViewById(R.id.textview_home_ride_title);

        cardButtonLayout = findViewById(R.id.layout_home_card);
        checkInButtonLayout = findViewById(R.id.layout_home_checkin);
        checkInButtonTitle = findViewById(R.id.textview_home_checkin_title);

        requestButtonLayout.setOnClickListener(v -> onClickAanvragen());
        cardButtonLayout.setOnClickListener(v -> onClickKaart());
        currentRideButtonLayout.setOnClickListener(v -> cancelRide());
        checkInButtonLayout.setOnClickListener(v -> {
            CheckInSheetDialogFragment bottomDialog = new CheckInSheetDialogFragment();
            bottomDialog.show(getSupportFragmentManager(), "fragment_checkin_dialog");
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(Style.LIGHT);
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(mapLat, mapLong))
                    .zoom(mapZoom)
                    .build());

            HomeActivity.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(Style.LIGHT, style -> new LoadGeoJson(HomeActivity.this).execute());
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        int currentRoute = PreferenceHelper.read(this, Globals.PrefKeys.CROUTE_ID, 0);

        if(currentRoute != 0)
            currentRideHomeButtons();
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

    @Override
    public void onDialogClose(DialogInterface dialog) {
        Globals.RouteState routeState = Globals.RouteState.fromInt(PreferenceHelper.read(this, Globals.PrefKeys.CROUTE_STATE, 0));

        switch (routeState) {
            case CHECKED:
                checkInButtonTitle.setText(getString(R.string.home_button_checkout));
                break;
            case FINISHED:
                PreferenceHelper.save(this, Globals.PrefKeys.CROUTE_ID, 0);
                recreate();
        }
    }

    public void onClickAanvragen(){
        openAcitivity(new Intent(HomeActivity.this, RequestActivity.class), true);
    }

    private void onClickKaart() {
        openAcitivity(new Intent(HomeActivity.this, CardActivity.class), true);
    }

    private void currentRideHomeButtons() {
        currentRideButtonLayout.setVisibility(View.VISIBLE);
        checkInButtonLayout.setVisibility(View.VISIBLE);

        requestButtonLayout.setVisibility(View.GONE);
        cardButtonLayout.setVisibility(View.GONE);

        currentRideButtonTitle.setText("± " + PreferenceHelper.read(this, Globals.PrefKeys.CROUTE_ATIME));

        if(Globals.RouteState.fromInt(PreferenceHelper.read(this, Globals.PrefKeys.CROUTE_STATE, 0)) == Globals.RouteState.CHECKED)
            checkInButtonTitle.setText(getString(R.string.home_button_checkout));
    }

    private void cancelRide() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.home_cancel_ride_title))
                .setMessage(getString(R.string.home_cancel_ride_sub))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    PreferenceHelper.save(this, Globals.PrefKeys.CROUTE_ID, 0);
                    recreate();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    /**
     * Add data to the map once the GeoJSON has been loaded
     * @param featureCollection returned GeoJSON FeatureCollection from the async task
     */
    private void initData(@NonNull FeatureCollection featureCollection) {
        LineString lineString = (LineString) featureCollection.features().get(0).geometry();
        routeCoordinateList = lineString.coordinates();

        if(mapboxMap != null) {
            Style style = mapboxMap.getStyle();

            if(style != null) {
                initSources(style, featureCollection);
                initSymbolLayer(style);
                initRunnable();
            }
        }
    }

    /**
     * Set up the repeat logic for moving the icon along the route.
     */
    private void initRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(routeCoordinateList.size() - 1 > count) {
                    Point nextLocation = routeCoordinateList.get(count + 1);

                    if(markerIconAnimator != null && markerIconAnimator.isStarted()) {
                        markerIconCurrentLocation = (LatLng) markerIconAnimator.getAnimatedValue();
                        markerIconAnimator.cancel();
                    }

                    try {
                        markerIconAnimator = ObjectAnimator.ofObject(latLngEvaluator, count == 0 ? new LatLng(mapLat, mapLong): markerIconCurrentLocation, new LatLng(nextLocation.latitude(), nextLocation.longitude())).setDuration(shuttleSpeed);
                        markerIconAnimator.setInterpolator(new LinearInterpolator());

                        markerIconAnimator.addUpdateListener(animatorUpdateListener);
                        markerIconAnimator.start();
                    } catch (Exception e) {
                        return;
                    }

                    count++;

                    if(routeCoordinateList.size() - 1 == count) {
                        count = 0;
                    }

                    handler.postDelayed(this, shuttleSpeed);
                }
            }
        };

        handler.post(runnable);
    }

    /**
     * Listener interface for when the ValueAnimator provides an updated value
     */
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LatLng animatedPosition = (LatLng) valueAnimator.getAnimatedValue();
                    if (dotGeoJsonSource != null) {
                        dotGeoJsonSource.setGeoJson(Point.fromLngLat(
                                animatedPosition.getLongitude(), animatedPosition.getLatitude()));
                    }
                }
            };

    /**
     * Add various sources to the map.
     */
    private void initSources(@NonNull Style loadedMapStyle, @NonNull FeatureCollection featureCollection) {
        dotGeoJsonSource = new GeoJsonSource(DOT_SOURCE_ID, featureCollection);
        loadedMapStyle.addSource(dotGeoJsonSource);
    }

    /**
     * Add the marker icon SymbolLayer.
     */
    private void initSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("ic_shuttle", Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_shuttle))));

        loadedMapStyle.addLayer(new SymbolLayer("symbol-layer-id", DOT_SOURCE_ID).withProperties(
                iconImage("ic_shuttle"),
                iconSize(0.5f),
                iconIgnorePlacement(true),
                iconAllowOverlap(true)
        ));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if(handler != null && runnable != null) {
            handler.post(runnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if(handler != null && runnable != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if(markerIconAnimator != null) {
            markerIconAnimator.cancel();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private static class LoadGeoJson extends AsyncTask<Void, Void, FeatureCollection> {
        private WeakReference<HomeActivity> weakReference;

        LoadGeoJson(HomeActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... voids) {
            try {
                HomeActivity activity = weakReference.get();
                if (activity != null) {
                    InputStream inputStream = activity.getAssets().open("matched_route.geojson");
                    return FeatureCollection.fromJson(convertStreamToString(inputStream));
                }
            } catch (Exception exception) {
                Log.e("error", exception.toString());
            }
            return null;
        }

        static String convertStreamToString(InputStream is) {
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

        @Override
        protected void onPostExecute(@Nullable FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            HomeActivity activity = weakReference.get();
            if (activity != null && featureCollection != null) {
                activity.initData(featureCollection);
            }
        }
    }

    private static final TypeEvaluator<LatLng> latLngEvaluator = new TypeEvaluator<LatLng>() {
        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    };
}
