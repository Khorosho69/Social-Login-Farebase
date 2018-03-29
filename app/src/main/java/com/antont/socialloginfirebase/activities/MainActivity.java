package com.antont.socialloginfirebase.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.antont.socialloginfirebase.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.internal.TwitterApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIItems();
    }

    private void setupUIItems() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView userProfileImage = findViewById(R.id.user_profile_image_view);

        if (user == null) {
            return;
        }
        Picasso.get()
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(userProfileImage);

        TextView userNameTextView = findViewById(R.id.user_name_text_view);
        userNameTextView.setText(user.getDisplayName());

        TextView userEmailTextView = findViewById(R.id.user_email_text_view);
        userEmailTextView.setText(user.getEmail());

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener((View view) -> logout());
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();

        LoginManager.getInstance().logOut();

        signOutFromGoogle();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    private void signOutFromGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);

        client.signOut();
        client.revokeAccess();
    }
}
