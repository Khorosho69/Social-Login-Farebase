package com.antont.socialloginfirebase.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.antont.socialloginfirebase.R;
import com.antont.socialloginfirebase.view_models.LoginViewModel;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "yay";

    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplication().getApplicationContext());

        setContentView(R.layout.activity_login);

        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        setupFacebookLoginButton();
    }

    private void setupFacebookLoginButton() {
        LoginButton loginButton = findViewById(R.id.facebook_login_button);

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mLoginViewModel.getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                mLoginViewModel.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mLoginViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
