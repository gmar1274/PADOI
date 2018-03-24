package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoTitle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
     * Get rrotView and animate imageView with
     * @param rootView
     */
    public void animate(View rootView){
        ///////////
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1000);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        final ConstraintLayout lay = (ConstraintLayout)rootView.findViewById(R.id.login_layout);
        lay.setAnimation(set);
        try {
            /**Get list of images and create a timer to update image view every timer second
             **/
            final String[] path = getAssets().list("gallery_images");
            //Log.d("PATHH ",Arrays.toString(path)+" -- "+getAssets().toString());
            new CountDownTimer(30000, 5000) {

                public void onTick(long millisUntilFinished) {
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
                        lay.setBackground(background);

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
    @Override
    public void asyncFBUser(AccessToken accessToken) {
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
        mainMenu(prof);
    }

@Override
    public void mainMenu(Profile prof) {
        if(prof != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", prof.getName());
            intent.putExtra("image_url", prof.getProfilePictureUri(PADOI.WIDTH, PADOI.HEIGHT).toString());
            intent.putExtra("id", prof.getId());
            startActivity(intent);
            finish();
        }
    }
}
