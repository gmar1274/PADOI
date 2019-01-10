package quants.portfolio.dev.project.app.com.padoi_v2.Project.Activities;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import quants.portfolio.dev.project.app.com.padoi_v2.Project.Interfaces.IPadoiAPI;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Models.Event;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.RVAdapters.EventsAdapter;
import quants.portfolio.dev.project.app.com.padoi_v2.Project.Utils.Utils;
import quants.portfolio.dev.project.app.com.padoi_v2.R;


public class MainActivity extends AppCompatActivity implements IPadoiAPI {

    private static final String TAG = MainActivity.class.toString();
    private EventsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mCardViewLoadingLayout;
    private RecyclerView mRecyclerView;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();//displays all views
        initVolley();//set up HTTP object
        getVideos(null,50,1);//make http request

    }

    private void initViews() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//turns the status bar text to be read if background is light.
        setLoading(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.rv_events);
        // use a linear layout manager
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoading(true);
                Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
                getVideosByCat("concert",10,1);
            }
        });
        ImageView imageViewMap = findViewById(R.id.imageViewMap);
        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapViewActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageViewSocial = findViewById(R.id.imageViewSocial);
        imageViewSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SocialActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLoading(boolean isLoading) {
        if (mCardViewLoadingLayout == null) mCardViewLoadingLayout = findViewById(R.id.cardView);
        final long duration = getResources().getInteger(R.integer.loading_duration);
        if (isLoading) {//start the animation
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
        } else {//stop the animation
            mCardViewLoadingLayout.animate().cancel();
            mCardViewLoadingLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Transition to @EventActivity. Here we want to show a fullscreen view of videos and event info.
     *
     * @param event
     */
    private void goToEventActivity(Pair[] pair, Event event) {
        // Intent intent = new Intent(this,EventActivity.class);
        Intent intent = new Intent(this, FullscreenActivity.class);
        intent.putExtra(Event.TAG, event);
        intent.putExtra(Event.TAG,mAdapter.getList());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pair);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void getVideosByCat(String cat, final int vid_num, int page_index) {

        String url = "https://pixabay.com/api/videos/?key=11174422-52934996ee44e82c36908e956&q="+cat+"&per_page="+vid_num+"&page="+page_index;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //debugLog(response.toString());
                        try {
                            int hits = response.getInt("totalHits");
                            JSONArray arr = response.getJSONArray("hits");
                            ArrayList<Event> events = new ArrayList<>();
                            for (int i = 0; i < vid_num; ++i) {
                                String url = arr.getJSONObject(i).getJSONObject("videos").getJSONObject("tiny").getString("url");
                                int id = arr.getJSONObject(i).getInt("id");
                                events.add(new Event(url,String.valueOf(id)));
                            }

                            displayVideos(events);
                        }catch (Exception e ){
                            Utils.debugLog(MainActivity.this,TAG,"ERROR..."+e.getMessage(),true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * A HTTP request using Volley.
     *
     * @param apikey     if any needed
     * @param vid_num    number of videos I want
     * @param page_index manual control of indexing from rest service.
     */
    @Override
    public void getVideos(String apikey, final int vid_num, int page_index) {

        String url = "https://pixabay.com/api/videos/?key=11174422-52934996ee44e82c36908e956&q=music&per_page="+vid_num+"&page="+page_index;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //debugLog(response.toString());
                        try {
                            int hits = response.getInt("totalHits");
                            JSONArray arr = response.getJSONArray("hits");
                            ArrayList<Event> events = new ArrayList<>();
                            for (int i = 0; i < vid_num; ++i) {
                                String url = arr.getJSONObject(i).getJSONObject("videos").getJSONObject("tiny").getString("url");
                                int id = arr.getJSONObject(i).getInt("id");
                                events.add(new Event(url,String.valueOf(id)));
                            }

                            displayVideos(events);
                        }catch (Exception e ){
                            Utils.debugLog(MainActivity.this,TAG,"ERROR..."+e.getMessage(),true);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                            error.printStackTrace();
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }

    private void displayVideos(ArrayList<Event> events) {
        setLoading(false);
        if(mAdapter == null) {
            // specify an adapter (see also next example)
            mAdapter = new EventsAdapter(this, events, new EventsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Pair[] pair, Event event) {
                    goToEventActivity(pair, event);
                }

                @Override
                public void onItemLongClick(Event item) {
                    Toast.makeText(MainActivity.this, "On LONG Click: " + item.getId(), Toast.LENGTH_SHORT).show();
                    setLoading(false);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.update(events);
        }
    }

    private void initVolley() {
        if(mRequestQueue!=null){
            mRequestQueue.stop();
            mRequestQueue=null;
        }
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024*100); // 1MB cap(1024*1024)

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();

    }
    @Override
    public void onDestroy(){
        if(mRequestQueue!=null)this.mRequestQueue.stop();
        this.mRequestQueue=null;
        super.onDestroy();
    }
}
