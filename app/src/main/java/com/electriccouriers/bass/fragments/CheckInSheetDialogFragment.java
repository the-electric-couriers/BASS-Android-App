package com.electriccouriers.bass.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.helpers.NfcManager;
import com.electriccouriers.bass.helpers.ProgressDialogHelper;
import com.electriccouriers.bass.interfaces.CheckInDialogCloseListener;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonElement;

import java.util.Random;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInSheetDialogFragment extends BottomSheetDialogFragment implements RequestListener {

    private ProgressBar spinner;
    private NfcManager nfcManager;

    private boolean NFCScanning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin_dialog, container, false);

        ImageView kaartFront = view.findViewById(R.id.popup_imageKaart);
        spinner = view.findViewById(R.id.popup_cardProgressBar);
        nfcManager = new NfcManager(getActivity());

        User mainUser = User.create(PreferenceHelper.read(getContext(), Globals.PrefKeys.MAIN_USER));
        String cardImageURL = Globals.API_BASEURL + "user/card/" + mainUser.getUserID() + "/" + mainUser.getFullName() + "/" + mainUser.getAccessCode();
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);

        GlideUrl glideUrl = new GlideUrl(cardImageURL, new LazyHeaders.Builder()
                .addHeader("Authorization", PreferenceHelper.read(getContext(), Globals.PrefKeys.UTOKEN)).build());

        Glide.with(this).load(glideUrl).apply(options).listener(this).into(kaartFront);

        kaartFront.setOnClickListener(v -> checkIn());

        nfcManager.onActivityCreate();
        nfcManager.setOnTagReadListener(tagRead -> {
            Toast.makeText(getContext(), "tag read:" + tagRead, Toast.LENGTH_LONG).show();
            checkIn();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcManager.onActivityResume();
    }

    @Override
    public void onPause() {
        nfcManager.onActivityPause();
        super.onPause();
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        spinner.setVisibility(View.GONE);
        return false;
    }

    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();

        if(activity instanceof CheckInDialogCloseListener)
            ((CheckInDialogCloseListener)activity).onDialogClose(dialog);
    }

    private void checkIn() {
        ProgressDialogHelper.getInstance(getActivity()).show();

        User mainUser = User.create(PreferenceHelper.read(getContext(), Globals.PrefKeys.MAIN_USER));
        API.service().checkIn(PreferenceHelper.read(getContext(), Globals.PrefKeys.UTOKEN), mainUser.getUserID(), mainUser.getAccessCode(), PreferenceHelper.read(getContext(), Globals.PrefKeys.CROUTE_ID, 0)).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                assert response.body() != null;
                if(response.isSuccessful() && response.body().getAsJsonObject().get("success").getAsBoolean()) {
                    checkInSuccessful();
                    return;
                }

                Log.e("Error", response.message());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
            }
        });
    }

    private void checkInSuccessful() {
        if(Globals.RouteState.fromInt(PreferenceHelper.read(getContext(), Globals.PrefKeys.CROUTE_STATE, 0)) == Globals.RouteState.REQUESTED) {
            PreferenceHelper.save(getContext(), Globals.PrefKeys.CROUTE_STATE, Globals.RouteState.CHECKED.ordinal());
        } else {
            PreferenceHelper.save(getContext(), Globals.PrefKeys.CROUTE_STATE, Globals.RouteState.FINISHED.ordinal());
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            ProgressDialogHelper.getInstance(getActivity()).hide();
            dismiss();
        }, new Random().nextInt((1000 - 800) + 1) + 800);
    }

    private Boolean checkNFCTag(String message) {
        Log.d("PARTS", message);
        return true;
    }

    private void scanNFC() {
        Log.d("NFC", "NFC");

        android.nfc.NfcManager manager = (android.nfc.NfcManager) getContext().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            NFCScanning = true;
        }
    }
}
