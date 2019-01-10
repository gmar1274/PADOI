package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
public class FullscreenActivity extends SwipeDimissActivity {
    public static final String TAG=FullscreenActivity.class.getCanonicalName();
    private ArrayList<Event> mTestData;
    private ViewPager mContentView;
    private Toolbar mToolbar;
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
        mContentView = findViewById(R.id.viewpager);
        mContentView.setAdapter(new EventStoriesPagerAdapter(this, mTestData, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mContentView.arrowScroll(View.FOCUS_RIGHT);
            }
        }));
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        // Attaching the layout to the toolbar object
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////
        final FrameLayout frameLayout =findViewById(R.id.frame_layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOverscreenPopup(event);
            }
        });
        /////////////////////make activity open/close from swipe gestures
        FrameLayout frameLayoutParentView = findViewById(R.id.fullscreen_content);
        this.setMySwipeDownAnimationDismissView(mContentView);
    }

    private void displayOverscreenPopup(Event event) {
        Intent intent = new Intent(this,TransparentActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent,options.toBundle());
    }
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
        //hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

   private void onActionHomePressed(){
        supportFinishAfterTransition();
   }
   @Override
    public void onPause(){
        super.onPause();
   }
   @Override
    public void onResume(){
        super.onResume();
   }

}
