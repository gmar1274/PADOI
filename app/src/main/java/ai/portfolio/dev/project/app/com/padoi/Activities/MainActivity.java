package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.LiveBandTask;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5000; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5; // 1 minute
    private LocationManager mLocationManager;
    private Location userLocation;
    private List<BandUser> band_users;

    /**
     * Our Broadcast Receiver. We get notified that the data is ready this way.
     */
   // private BroadcastReceiver receiver;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String name = null, image_url = null, id = null;
        try {
            ///////////////////Get Extras
            Bundle login_bundle = getIntent().getExtras();
            name = login_bundle.getString("name").toString();
            image_url = login_bundle.getString("image_url");
            id = login_bundle.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
///////////
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView iv = (ImageView) headerView.findViewById(R.id.fbUserImageView);
        getUserPic(iv, image_url);
        TextView name_tv = (TextView) headerView.findViewById(R.id.user_name_tv);
        name_tv.setText(name);
/**
 * CUSTOM APP LOGIC. REST calls: search for live bands, start service (gps), gui drawer activity.
 * Need to register a BroadcastReciever with an IntentFilter
 */
        /*this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String response = intent.getStringExtra(PADOI.HTTP_RESPONSE_LIVE_BANDS);
                if (response != null) {//need to parse the JSON OBJECT
                    //parse the array of users from Firebase db

                    new Notify(context).createNotification().fromUser(new FBUser()).message("HI").sendNotification();
                   // Log.d("IN On_RECIEVE:", "RESPONSE = " + response);
                   // band_users = PADOI.toList(response, BandUser.class);
                   Toast.makeText(context,"USER: "+band_users.get(0).getName()+" .... OBJ: "+band_users.get(0),Toast.LENGTH_LONG).show();
                    //Log.d("JSON DESERIALIZE:",band_users.toString());
                    //Toast.makeText(context, "onRec: " + band_users, Toast.LENGTH_LONG).show();
                    //Toast.makeText(context,"SIZE: "+band_adapter.getSize()+" < > "+band_users.size(),Toast.LENGTH_LONG).show();
                } else {
                    Log.d("DEBUG_RES", "NULL RESPONSE");
                }
            }
        };*/
        //IntentFilter filter = new IntentFilter(PADOI.HTTP_RESPONSE_LIVE_BANDS);
        //this.registerReceiver(receiver, filter);
        customGUISettings(headerView, this.findViewById(android.R.id.content).getRootView());
        requestUserLocation();

    }

    /**
     * REST API. Firebase. Search by user location. Update the list view for TRENDING
     */
    private void searchLiveBands(RecyclerView rv) {
        new LiveBandTask(this,rv).execute();
    }

    /**
     * Start a service to get gps. Check permission.
     */
    private void requestUserLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                return;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param headerView - from navigation View
     * @param rootView   - from root view
     */
    private void customGUISettings(View headerView, View rootView) {
        if(band_users==null)this.band_users=new ArrayList<>();
        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.recycle_view_BANDS);
        searchLiveBands(rv);
        ///////////////////////////////HORIZONTAL VIEW USING RECYCLE VIEW
        //Customize SNACK BAR
       /* Snackbar snackbar = Snackbar.make(rootView.findViewById(R.id.main_layout),"", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
// Hide the text*/
       // TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
       // textView.setVisibility(View.INVISIBLE);

// Inflate our custom view
     //   View snackView = this.getLayoutInflater().inflate(R.layout.test, null);
     //   TextView tv = (TextView)snackView.findViewById(R.id.nameTEST_TV);
     //   final LinearLayout info = (LinearLayout)snackView.findViewById(R.id.info_layout);
     //   tv.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
      //          info.setVisibility(info.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
      //      }
      //  });
// Add the view to the Snackbar's layout
       // layout.addView(snackView, 0);
// Show the Snackbar
       // snackbar.show();
    }

    /**
     * Async request get user fb pic.
     *
     * @param iv
     */
    private void getUserPic(ImageView iv, String url) {
        new DownLoadImageTask(this.getApplicationContext(),iv).execute(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.fb_log_out_id) {
            logOut();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        Toast.makeText(this, "Logged Out.", Toast.LENGTH_LONG).show();
        Intent moveToMain = new Intent(this, LoginActivity.class);
        moveToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        moveToMain.putExtra("EXIT", true);
        startActivity(moveToMain);
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestUserLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this.getApplicationContext(),"Location: "+location,Toast.LENGTH_LONG).show();
        this.userLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BandUser.class.toString(), (ArrayList<? extends Parcelable>) this.band_users);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       band_users = savedInstanceState.getParcelableArrayList(BandUser.class.toString());
    }

}