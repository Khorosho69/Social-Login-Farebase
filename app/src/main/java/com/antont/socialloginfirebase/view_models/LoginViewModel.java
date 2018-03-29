package com.antont.socialloginfirebase.view_models;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.antont.socialloginfirebase.R;
import com.antont.socialloginfirebase.activities.MainActivity;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.concurrent.Executor;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "yay";
    public static int RC_SIGN_IN = 1001;

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public CallbackManager getCallbackManager() {
        if (mCallbackManager == null) {
            mCallbackManager = CallbackManager.Factory.create();
        }
        return mCallbackManager;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            @SuppressLint("RestrictedApi")
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                handleSignInResult(credential);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        } else  {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void handleSignInResult(AuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(Runnable::run, task -> {
            if (task.isSuccessful()) {
                startMainActivity();
                Log.d(TAG, "onComplete: ");
            } else {
                Log.d(TAG, "onFail: ");
            }
        });
    }

    public Intent getGoogleSignInIntent() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getApplication().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplication().getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    public void startMainActivity() {
        Intent intent = new Intent(getApplication().getApplicationContext(), MainActivity.class);
        getApplication().getApplicationContext().startActivity(intent);
    }
}
