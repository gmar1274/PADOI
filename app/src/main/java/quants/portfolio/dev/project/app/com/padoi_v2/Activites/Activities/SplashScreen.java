package quants.portfolio.dev.project.app.com.padoi_v2.Activites.Activities;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.facebook.Profile;

import quants.portfolio.dev.project.app.com.padoi_v2.Activites.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class SplashScreen extends AppCompatActivity {
    private TextView tv;
    private boolean mShouldFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tv = findViewById(R.id.textView);
        CardView cv = findViewById(R.id.cardView);
        long duration = getResources().getInteger(R.integer.loading_duration);
        cv.animate().setInterpolator(new FastOutSlowInInterpolator()).rotation(Utils.LOADING_ROTATION).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //if logged in then go to main activity
                if(Profile.getCurrentProfile() != null){
                    goToMainActivity(intent);
                }else{//Otherwise first time user
                    goToLoginActivity(intent);
                }
                mShouldFinish=true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mShouldFinish)
        {
            finish();
        }
    }
    private void goToLoginActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api 21
            // Apply activity transition
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(new Intent(this,LoginAPIActivity.class),options.toBundle());
        } else {
            // Swap without transition
            startActivity(new Intent(this,LoginAPIActivity.class));
        }

    }

    private void goToMainActivity(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api 21
            // Apply activity transition
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.<View, String>create(tv,tv.getTransitionName()));
            startActivity(intent,options.toBundle());
        } else {
            // Swap without transition
            startActivity(intent);
        }
    }
    @Override
    public void finish(){
        super.finish();
    }
}
