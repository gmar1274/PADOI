package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.Profile;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.ISplashScreen;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * This activity will be the main point of entry to the app. It will be used to animate a logo on entry to app and for Authenticating purposes.
 * This will check to see if user is already logged in wikth FB LoginManager.
 */
public class SplashScreenActivity extends AppCompatActivity implements ISplashScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setInterpolator(new LinearInterpolator());
        final ImageView im = (ImageView)findViewById(R.id.splash_imageView);
        im.setAnimation(rotate);






        AccessToken token = AccessToken.getCurrentAccessToken();
        Profile prof = Profile.getCurrentProfile();
        if(token!=null && prof!=null ){
         continueToApp(prof);
        }else{
            login();
        }
        finish();
    }

    @Override
    public void continueToApp(Profile prof) {
            if(prof!=null) {

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", prof.getName());
                intent.putExtra("image_url", prof.getProfilePictureUri(PADOI.WIDTH, PADOI.HEIGHT).toString());
                intent.putExtra("id", prof.getId());
                startActivity(intent);
                finish();
            }
    }

    @Override
    public void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
