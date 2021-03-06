package com.electriccouriers.bass.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.helpers.VerifyEmail;
import com.electriccouriers.bass.models.Company;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;

public class LoginActivity extends BaseActivity {

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = findViewById(R.id.welcomeEmailVeld);
        password = findViewById(R.id.welcomeWachtwoordVeld);

        findViewById(R.id.loginButton2).setOnClickListener(v -> onClickLoginButton2());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.login_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_back_black;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    /**
     *Geschreven door Nikki Valkenburg
     * Onclicklistener die word gebruikt om login informatie te controleren.
     * een paar simpele if statements om te controleren of data is ingevuld.
     * verifyEmail is geschreven door Tan Bui
     * valideUserCredentials(); is geschreven door Thomas Hopstaken
     */

    public void onClickLoginButton2() {
        VerifyEmail verifyEmail = new VerifyEmail();
        boolean error = false;

        if (email.getText().toString().isEmpty()) {
            email.setError(getString(R.string.login_leeg_email_bericht));
            error = true;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.login_leeg_password));
            error = true;
        }

        if (error) {
            showError(getString(R.string.login_form_error));
            return;
        }

        if (!verifyEmail.verify(email.getText().toString())) {
            email.setError(getString(R.string.login_verkeerd_email_bericht));
            showError(getString(R.string.login_verkeerd_email_bericht));
            return;
        }

        valideUserCredentials();
    }

    private void valideUserCredentials() {

        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        API.service().login(emailString, passwordString).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    userLoginFailed();
                    return;
                }

                User responseUser = response.body();
                responseUser.setCompany(new Company(responseUser.getCompanyID(), responseUser.getCompanyName(), responseUser.getRoutePointID()));
                if (responseUser.getToken() == null) {
                    userLoginFailed();
                    return;
                }

                successfulLogin(responseUser);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userLoginFailed();
            }

        });
    }

    private void successfulLogin(User user) {
        PreferenceHelper.save(this, Globals.PrefKeys.MAIN_USER, user.serialize());
        PreferenceHelper.save(this, Globals.PrefKeys.UTOKEN, "Token " + user.getToken());

        Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        openAcitivity(homeActivity);
    }

    private void userLoginFailed() {
        new AlertDialog.Builder(this)
                .setTitle("Foutmelding")
                .setMessage("Uw email en/of wachtwoord wordt niet herkend.")
                .show();

    }
}
