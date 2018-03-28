package com.antont.socialloginfirebase;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
