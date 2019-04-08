package com.electriccouriers.bass.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.electriccouriers.bass.R;

public class ProgressDialogHelper {

    public static ProgressDialogHelper instance;

    private ProgressDialog progressDialog;
    private Context context;

    public ProgressDialogHelper(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    public static ProgressDialogHelper getInstance(Context context) {
        if(instance == null)
            instance = new ProgressDialogHelper(context);

        return instance;
    }

    public void show() {
        progressDialog.show();
        setupDialog();
    }

    public void hide() {
        progressDialog.hide();
        instance = null;
    }

    private void setupDialog() {
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparant);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
    }
}

