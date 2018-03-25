package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.ILoginSuccess;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.IUserAuth;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

public class LoginActivity extends AppCompatActivity implements IUserAuth, ILoginSuccess {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private int index=0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       //setTheme(R.style.AppTheme_NoTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
      /*  if (getIntent().getBooleanExtra("EXIT", false))
        {
           // finish();
            //return;
        }*/
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setTextSize(25);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //asyncFBUser(loginResult.getAccessToken());
                        Profile prof = Profile.getCurrentProfile();
                        mainMenu(prof);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            /**
             * The method that will be called with the access token changes.
             *
             * @param oldAccessToken     The access token before the change.
             * @param currentAccessToken The new access token.
             */
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                
            }
        };
        accessTokenTracker.startTracking();
        profileTracker = new ProfileTracker() {
            /**
             * The method that will be called when the profile changes.
             *
             * @param oldProfile     The profile before the change.
             * @param currentProfile The new profile.
             */
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                mainMenu(currentProfile);
            }
        };
        profileTracker.startTracking();
        View rootView =this.findViewById(android.R.id.content).getRootView();
        animate(rootView);
    }

    /**
     * Get rrotView and animate imageView with.
     * This is bad code but it works. Get image paths from asset folder.
     * Put a countdown and animate a fade for each image in a loop.
     * @param rootView
     */
    public void animate(View rootView){
        /*
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        final FrameLayout lay = (FrameLayout) rootView.findViewById(R.id.login_layout);
        lay.setAnimation(set);*/
        try {
            /**Get list of images and create a timer to update image view every timer second
             **/
            final String[] path = getAssets().list("gallery_images");
            //Log.d("PATHH ",Arrays.toString(path)+" -- "+getAssets().toString());
            new CountDownTimer(40000, 5000) {

                public void onTick(long millisUntilFinished) {
                    //AnimationSet set = new AnimationSet(true);

                    Animation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(2000);
                    //set.addAnimation(animation);
                    final ImageView iv = (ImageView)findViewById(R.id.login_gallery_IV);
                    iv.startAnimation(animation);
                    if(index+1==path.length)index=0;
                    else index += 1;
                    try {
                         /* adapt the image to the size of the display */
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        InputStream is = LoginActivity.this.getAssets().open("gallery_images/" + path[index].toString());
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Bitmap bmp = Bitmap.createScaledBitmap(bitmap,size.x,size.y,true);

                        BitmapDrawable background = new BitmapDrawable(bmp);
                        iv.setImageBitmap(bmp);
                        //lay.setBackground(background);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    //mTextField.setText("done!");
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
@Override
protected void onStop(){
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
}

    /**
     * Sign in with firebase Auth
     * and fetch FB API
     * @param accessToken
     */
    @Override
    public void asyncFBUser(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                           // Log.d("Firebase sign in", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            GraphRequest request = GraphRequest.newGraphPathRequest(
                                    accessToken,
                                    "/{"+accessToken.getUserId()+"}",
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {

                                            Profile prof =  getProfileFromFraphResponse(response); // Insert your code here
                                            mainMenu(prof);
                                        }
                                    });
                            request.executeAsync();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("firebase sign in", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(FacebookLoginActivity.this, "Authentication failed.Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                        // ...
                    }
                });

    }

    /**
     * Respond to FB api by continuing to app because login was a success.
     * Go to MainActivity and finish with login process. Future login credential is handled by FB Login Manager
     * @param response
     */
    private Profile getProfileFromFraphResponse(GraphResponse response) {
        Profile prof = getFBProfile(response);
       return prof;
    }

    @Override
    public Profile getFBProfile(GraphResponse graphResponse) {
        return Profile.getCurrentProfile();
    }
    @Override
    protected void onPause(){super.onPause();}
    @Override
    protected void onResume(){
        super.onResume();
        Profile prof = Profile.getCurrentProfile();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mainMenu(prof);
    }

@Override
    public void mainMenu(Profile prof) {
        if(prof != null) {
            Log.d("firebase sign in", "signInWithCredential:SUCCESSS");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", prof.getName());
            intent.putExtra("image_url", prof.getProfilePictureUri(PADOI.WIDTH, PADOI.HEIGHT).toString());
            intent.putExtra("id", prof.getId());
            startActivity(intent);
            finish();
        }
    }
}
