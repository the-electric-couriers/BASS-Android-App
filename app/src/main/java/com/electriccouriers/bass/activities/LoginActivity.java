package com.electriccouriers.bass.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.widget.EditText;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.helpers.VerifyEmail;

public class LoginActivity extends BaseActivity {

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = findViewById(R.id.welcomeEmailVeld);
        password = findViewById(R.id.welcomeWachtwoordVeld);

        findViewById(R.id.loginButton2).setOnClickListener(v -> {
           onClickLoginButton2();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
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

    public void onClickLoginButton2() {
        VerifyEmail verifyEmail = new VerifyEmail();

        if(email.getText().toString().isEmpty()) {
            //TODO: Leeg email veld error
            return;
        }

        if(!verifyEmail.verify(email.getText().toString())) {
            //TODO: Geen valid email error
            return;
        }

        if(password.getText().toString().isEmpty()) {
            //TODO: Leeg password veld error
            return;
        }

        //TODO: Gegevens naar sever doorgeven, liefste met aparte method
        valideUserCredentials();

    }

    private void valideUserCredentials() {
        System.out.println("Gegevens mogen naar de server");
    }
}
