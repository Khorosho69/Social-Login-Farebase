package com.antont.socialloginfirebase.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "yay";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;


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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(Runnable::run, task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d(TAG, "onComplete: ");
            } else {
                // If sign in fails, display a message to the user.
                Log.d(TAG, "onFail: ");
            }
        });
    }
}
