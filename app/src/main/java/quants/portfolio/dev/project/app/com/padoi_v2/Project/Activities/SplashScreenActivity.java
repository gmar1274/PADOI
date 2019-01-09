package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.List;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class SplashScreenActivity extends AppCompatActivity implements PermissionsListener {
    private TextView mTextView;
    private boolean mShouldFinish;//needed for animation transition. when its gone delete activity.
    private PermissionsManager permissionsManager;
    private CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initViews();
        enableLocation();
    }
    private void initViews() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//turns the status bar text to be read if background is light.
        mTextView = findViewById(R.id.textView);
        mCardView = findViewById(R.id.cardView);
        Utils.setLoading(this, mCardView,true);
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
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, Pair.<View, String>create(mTextView, mTextView.getTransitionName()));
            startActivity(intent,options.toBundle());
        } else {
            // Swap without transition
            startActivity(intent);
        }
    }
    private void enableLocation(){

        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //if logged in then go to main activity
            if (Profile.getCurrentProfile() != null) {
                goToMainActivity(intent);
            } else {//Otherwise first time user
                goToLoginActivity(intent);
            }
            mShouldFinish = true;
        }else {

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @Override
    public void finish(){
        super.finish();
    }
}
