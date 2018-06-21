package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.ILoginAuthHandler;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.ISplashScreen;
import ai.portfolio.dev.project.app.com.padoi.R;

/**
 * This activity will be the main point of entry to the app. It will be used to animate a logo on entry to app and for Authenticating purposes.
 * This will check to see if user is already logged in wikth FB LoginManager.
 */
public class SplashScreenActivity extends AppCompatActivity implements ISplashScreen, ILoginAuthHandler {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView iv = (ImageView)findViewById(R.id.splash_iv);
        ObjectAnimator  an = ObjectAnimator.ofFloat(iv,
                "rotation", 0f, 360f);
        an.setDuration(1000); // miliseconds
        an.start();

        mAuth = FirebaseAuth.getInstance();
/***
 * Get login manager, check if already logged in. If it is Login in via Firebase AUTH
 and continue to main activity. Otherwise go to LoginAcctivity for first time user */
/*
        AccessToken token = AccessToken.getCurrentAccessToken();
        final Profile prof = Profile.getCurrentProfile();
        if(token!=null && prof!=null ){
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                               // Log.d("Firebase sign in", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                continueToApp(prof);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                 throw networkError(null);
                            }

                            // ...
                        }
                    });
        }else{
          */  an.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    login();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        //}
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            continueToApp(currentUser);
        }
    }
    @Override
    public void continueToApp(FirebaseUser prof) {
            if(prof!=null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", prof.getDisplayName());
                intent.putExtra("image_url", prof.getPhotoUrl().toString());
                intent.putExtra("id", prof.getUid());
                startActivity(intent);
                finish();
            }
    }

    @Override
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public RuntimeException networkError(String errMessage) {
        Toast.makeText(this, "Authentication failed. Restart app.",Toast.LENGTH_LONG).show();
        return errMessage==null? new RuntimeException("Network authorization error."): new RuntimeException(errMessage);
    }
}