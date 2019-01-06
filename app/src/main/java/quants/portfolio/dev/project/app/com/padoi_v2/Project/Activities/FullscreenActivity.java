package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.EventStoriesPagerAdapter;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    public static final String TAG=FullscreenActivity.class.getCanonicalName();
    private ArrayList<Event> mTestData;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    //private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            // mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private Toolbar mToolbar;
    //private VideoView mVideoView;
    private int mStopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Event event = getIntent().getParcelableExtra(Event.TAG);
        mTestData = getIntent().getParcelableArrayListExtra(Event.TAG);
        Utils.debugLog(this,TAG,mTestData.toString(),false);
        initViews(event);

    }

    private void initViews(final Event event) {
        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new EventStoriesPagerAdapter(this, mTestData, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                viewPager.arrowScroll(View.FOCUS_RIGHT);
            }
        }));


        final ProgressBar progressBar = findViewById(R.id.progressBar);
        //TextView tv = findViewById(R.id.textView);
        //tv.setText(event.getId());
        mVisible = true;
        // = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        // Attaching the layout to the toolbar object
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*mVideoView = findViewById(R.id.videoView);
        mVideoView = Utils.prepareVideo(this, mVideoView,event);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                mp.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onActionHomePressed();//exit activity
            }
        });*/

        //////////////
        FrameLayout frameLayout =findViewById(R.id.frame_layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOverscreenPopup(event);
            }
        });
    }

    private void displayOverscreenPopup(Event event) {
        Intent intent = new Intent(this,TransparentActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent,options.toBundle());
    }

    // Set up the user interaction to manually show or hide the system UI.

    /**
     * mContentView.setOnClickListener(new View.OnClickListener() {
     *
     * @Override public void onClick(View view) {
     * toggle();
     * }
     * });
     **/

    // Upon interacting with UI controls, delay any scheduled hide()
    // operations to prevent the jarring behavior of controls going away
    // while interacting with the UI.
    //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    //}@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onActionHomePressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
   private void onActionHomePressed(){
        supportFinishAfterTransition();
   }
   @Override
    public void onPause(){
        super.onPause();
        /*if(mVideoView!= null){
            mVideoView.pause();
            mStopPosition = mVideoView.getCurrentPosition(); //stopPosition is an int
        }*/
   }
   @Override
    public void onResume(){
        super.onResume();
        /*if(mVideoView != null){
            mVideoView.seekTo(mStopPosition);
            mVideoView.start();
        }*/
   }

}
