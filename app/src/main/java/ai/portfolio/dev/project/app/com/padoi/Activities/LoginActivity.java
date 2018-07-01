package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.IPadoiAuth;
import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

public class LoginActivity extends AppCompatActivity implements IPadoiAuth{
        private static final int TAG_CODE_PERMISSION_LOCATION =1 ;
        private static final String TAG = "firebase";
        private CallbackManager mCallbackManager;
        private FirebaseAuth mAuth;
        private int index=0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            mAuth = FirebaseAuth.getInstance();
            // Initialize Facebook Login button
            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = findViewById(R.id.fb_sign_in);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });
            View rootView =this.findViewById(android.R.id.content).getRootView();
            animate(rootView);
            requestUserLocation();

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
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK

            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }

        @Override
        public void onStart() {
            super.onStart();
        }

    /**
     *
     * @param token api auth token from fb
     */
    private void handleFacebookAccessToken(AccessToken token) {
           // Log.d(TAG, "handleFacebookAccessToken:" + token);
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithCredential:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                final PadoiUser padoiUser = new PadoiUser(user);
                                DatabaseReference ref = addNewPadoiUser(user);
                                ref.setValue(padoiUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())continueToApp(padoiUser);
                                            else {
                                                Toast.makeText(LoginActivity.this,"Network error :( ",Toast.LENGTH_LONG).show();
                                                Log.e("Firebase API error","Database write error");
                                                finish();
                                            }
                                    }
                                });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e("Firebase API error","Auth token fb sdk error");
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    /**
     * Continues to main app with  PADIO  user from firebase db.
     * @param padoiUser
     */
    public void continueToApp(PadoiUser padoiUser) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(PADOI.USER, padoiUser);
            intent.putExtras(mBundle);
            startActivity(intent);
            finish();
        }
        /**
         * Start a service to get gpsLoader. Check permission.
         */
        private void requestUserLocation() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //the casee user is on updated OS runtime permission is needed to ask.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, TAG_CODE_PERMISSION_LOCATION);
                }
            } else {
                //requestUserLocation();
                //permission to get location
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case TAG_CODE_PERMISSION_LOCATION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {//user did not grant permission
                        Toast.makeText(this, "Permission to access GPS was denied. :(", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    @Override
    public DatabaseReference fetchPadoiUser(FirebaseUser user) {
        return null;
    }

    @Override
    public DatabaseReference addNewPadoiUser(FirebaseUser user) {
        return FirebaseDatabase.getInstance().getReference().child(PADOI.DBPATH_USERS+"/"+user.getUid());
    }
}