package com.antont.socialloginfirebase.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.antont.socialloginfirebase.R;
import com.antont.socialloginfirebase.view_models.LoginViewModel;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "yay";

    private LoginViewModel mLoginViewModel;
    private TwitterLoginButton mTwitterLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplication().getApplicationContext());
        initTwitterAuthConfig();
        setContentView(R.layout.activity_login);

        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            mLoginViewModel.startMainActivity();
        } else {
            setupFacebookLoginButton();
            setupGoogleLoginButton();
            setupTwitterLoginButton();
        }
    }

    private void initTwitterAuthConfig() {
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);
    }

    private void setupGoogleLoginButton() {
        SignInButton googleLoginButton = findViewById(R.id.google_login_button);

        googleLoginButton.setOnClickListener((View view) ->
                startActivityForResult(mLoginViewModel.getGoogleSignInIntent(), LoginViewModel.RC_SIGN_IN));
    }


    private void setupFacebookLoginButton() {
        LoginButton facebookLoginButton = findViewById(R.id.facebook_login_button);

        facebookLoginButton.setReadPermissions("email", "public_profile");

        facebookLoginButton.registerCallback(mLoginViewModel.getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

                mLoginViewModel.handleSignInResult(credential);
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

    private void setupTwitterLoginButton() {
        mTwitterLoginButton = findViewById(R.id.twitter_login_button);

        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);

                AuthCredential credential = TwitterAuthProvider.getCredential(
                        result.data.getAuthToken().token,
                        result.data.getAuthToken().secret);

                mLoginViewModel.handleSignInResult(credential);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
            }
        });
    }

//    private void setupInstagramloginButton(){
//        Button instagramLoginButton = findViewById(R.id.instagram_login_button);
//
//        instagramLoginButton.setOnClickListener((View view) -> instagramButtonOnClick());
//    }
//
//    private void instagramButtonOnClick(){
//        final Uri.Builder uriBuilder = new Uri.Builder();
//        uriBuilder.scheme("https")
//                .authority("api.instagram.com")
//                .appendPath("oauth")
//                .appendPath("authorize")
//                .appendQueryParameter("client_id", "c3fb34aad7e6408a86b33c6127983edd")
//                .appendQueryParameter("redirect_uri", "https://www.google.ru/")
//                .appendQueryParameter("response_type", "token");
//        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
//        startActivity(browser);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        } else
            mLoginViewModel.onActivityResult(requestCode, resultCode, data);
    }
}
