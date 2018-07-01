package ai.portfolio.dev.project.app.com.padoi.Activities;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.ILoginAuthHandler;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.IPadoiAuth;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.ISplashScreen;
import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * This activity will be the main point of entry to the app. It will be used to animate a logo on entry to app and for Authenticating purposes.
 * This will check to see if user is already logged in with FB LoginManager.
 * At the end of Animation:
 * 1) if user is logged in then continue to app
 * 2) otherwise go to login activity to login
 */
public class SplashScreenActivity extends AppCompatActivity implements ISplashScreen, ILoginAuthHandler, IPadoiAuth, Animator.AnimatorListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView iv = (ImageView) findViewById(R.id.splash_iv);
        ObjectAnimator an = animateSplash(iv);
        an.setDuration(1000); // miliseconds
        an.start();
        an.addListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void continueToApp(PadoiUser padoiUser) {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(PADOI.USER, padoiUser);
        intent.putExtras(mBundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public ObjectAnimator animateSplash(ImageView iv) {
        return ObjectAnimator.ofFloat(iv,
                "rotation", 0f, 360f);
    }

    @Override
    public RuntimeException networkError(String errMessage) {
        Toast.makeText(this, "Authentication failed. Restart app.", Toast.LENGTH_LONG).show();
        return errMessage == null ? new RuntimeException("Network authorization error.") : new RuntimeException(errMessage);
    }


    @Override
    public void onAnimationStart(Animator animation) {


    }

    @Override
    public void onAnimationEnd(Animator animation) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) login();
        else {
            DatabaseReference ref = fetchPadoiUser(user);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PadoiUser padoiUser = dataSnapshot.getValue(PadoiUser.class);
                    if (padoiUser == null) {
                        login();
                    } else {
                        continueToApp(padoiUser);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Known bug: with empty database the app will go here...so for now we assume new user so go to login screen
                    Log.e("Database error firebase", "SplashScreenActivity: error: " + databaseError.getMessage());
                    login();
                }
            });
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public DatabaseReference fetchPadoiUser(FirebaseUser user) {
        return FirebaseDatabase.getInstance().getReference(PADOI.DBPATH_USERS + "/" + user.getUid());
    }

    @Override
    public DatabaseReference addNewPadoiUser(FirebaseUser user) {
        return null;
    }
}