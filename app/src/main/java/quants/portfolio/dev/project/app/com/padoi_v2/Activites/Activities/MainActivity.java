package quants.portfolio.dev.project.app.com.padoi_v2.Activites.Activities;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import quants.portfolio.dev.project.app.com.padoi_v2.Activites.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.Activites.RVAdapters.EventsAdapter;
import quants.portfolio.dev.project.app.com.padoi_v2.Activites.TEST.TEST;
import quants.portfolio.dev.project.app.com.padoi_v2.Activites.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="Main" ;
    private EventsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mCardViewLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /////set loading layout until loaded
        setLoading(true);


        RecyclerView mRecyclerView = findViewById(R.id.rv_events);
        // use a linear layout manager
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new EventsAdapter(this, TEST.generateTestEvents(50),new EventsAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Pair[] pair,Event event) {
                goToEventActivity(pair,event);
            }

            @Override
            public void onItemLongClick(Event item) {
                Toast.makeText(MainActivity.this,"On LONG Click: "+item.getId(),Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this,"Refreshing...",Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void setLoading(boolean isLoading) {
       if(mCardViewLoadingLayout ==null) mCardViewLoadingLayout = findViewById(R.id.cardView);
       final long duration = getResources().getInteger(R.integer.loading_duration);
       if(isLoading){//start the animation
           mCardViewLoadingLayout.setVisibility(View.VISIBLE);
           mCardViewLoadingLayout.animate().setInterpolator(new AnticipateOvershootInterpolator()).setDuration(duration).rotation(Utils.LOADING_ROTATION).setListener(new Animator.AnimatorListener() {
               @Override
               public void onAnimationStart(Animator animation) {

               }

               @Override
               public void onAnimationEnd(Animator animation) {
                   mCardViewLoadingLayout.animate().setListener(this); //It listens for animation's ending and we are passing this to start onAniationEnd method when animation ends, So it works in loop
                   mCardViewLoadingLayout.setRotation(0);
                   mCardViewLoadingLayout.animate().setInterpolator(new AnticipateOvershootInterpolator()).setStartDelay(300).setDuration(duration).rotation(Utils.LOADING_ROTATION).start();
               }

               @Override
               public void onAnimationCancel(Animator animation) {

               }

               @Override
               public void onAnimationRepeat(Animator animation) {

               }
           }).start();
       }else{//stop the animation
           mCardViewLoadingLayout.animate().cancel();
           mCardViewLoadingLayout.setVisibility(View.GONE);
       }
    }

    /**
     * Transition to @EventActivity
     * @param event
     */
    private void goToEventActivity(Pair[]  pair,Event event) {
        Intent intent = new Intent(this,EventActivity.class);
        intent.putExtra(Event.TAG,event);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pair);
        startActivity(intent , options.toBundle());
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override public void finish() {
        super.finish();
    }

}
