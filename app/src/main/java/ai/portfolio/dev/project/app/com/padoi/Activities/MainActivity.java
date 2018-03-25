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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.Fragments.MapViewFragment;
import ai.portfolio.dev.project.app.com.padoi.Fragments.TrendingFragment;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.IFirebase;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.Models.FBUser;
import ai.portfolio.dev.project.app.com.padoi.Models.PADOIUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, IFirebase {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5000; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5; // 1 minute
    private static final String TAG = "FIREBASE TAG";
    private static final int TAG_CODE_PERMISSION_LOCATION =100 ;
    private LocationManager mLocationManager;
    private Location userLocation;

    private List<BandUser> band_users;
    private PADOIUser currentUser;
    private FBUser fbUser;
    private ProgressBar pb;
    private FusedLocationProviderClient mFusedLocationClient;

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
            fbUser = new FBUser(name, image_url, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        customGUISettings(headerView, this.findViewById(android.R.id.content).getRootView());
        ///////////////////////////
        requestUserLocation();

    }


    /**
     * Start a service to get gps. Check permission.
     */
    private void requestUserLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            ///////////////////////////////////////////////////////
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                userLocation = location;
                                Toast.makeText(MainActivity.this,"LOCATION: "+location.toString(),Toast.LENGTH_LONG).show();// Logic to handle location object
                            }
                            Log.d("DEBUGGGGG","HERERRRRRRRRRR");
                        }
                    });
            ///////////////////////////////////////////////////////
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
            }else{
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        TAG_CODE_PERMISSION_LOCATION);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * @param headerView - from navigation View
     * @param rootView   - from root view
     */
    private void customGUISettings(View headerView, View rootView) {
        if (band_users == null) this.band_users = new ArrayList<>();
        displayFragmentScreen(R.id.live_menu_item);
        pb = (ProgressBar) rootView.findViewById(R.id.progress_bar_main);

    }

    /**
     * Async request get user fb pic.
     *
     * @param iv
     */
    private void getUserPic(ImageView iv, String url) {
        new DownLoadImageTask(this.getApplicationContext(), iv).execute(url);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.fb_log_out_id) {
            logOut();
        } else {
            displayFragmentScreen(id);
        }
        return true;
    }

    /**
     * Switch on id from the menu option method
     *
     * @param id
     * @return
     */
    public void displayFragmentScreen(int id) {
        Fragment frag = null;
        switch (id) {
            case R.id.mapView_menu_item:
                frag = new MapViewFragment();
                break;
            case R.id.live_menu_item:
                if (currentUser == null && userLocation==null)return;//wait until location is updated then the program will run
                frag = new TrendingFragment().setUser(currentUser);
                break;
        }
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_layout, frag);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    /**
     * Fetch PADOIUser from firebase database
     *
     * @param fb_ID
     */
    @Override
    public void fetchFirebaseUser(String fb_ID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(PADOI.DBPATH_USERS);
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createNewUser(MainActivity.this.fbUser);
                } else {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    currentUser = dataSnapshot.getValue(PADOIUser.class);
                    goToTrendingActivity(currentUser);
                    Log.d(TAG, "Value is: " + currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void goToTrendingActivity(PADOIUser currentUser) {
        pb.setVisibility(View.GONE);
        displayFragmentScreen(R.id.live_menu_item);
    }

    /**
     * Fetch all band users from pagination
     *
     * @param band_users list of bands for the program
     * @param pagination when needed to load more bands
     */
    @Override
    public void fetchFirebaseBandUsers(List<BandUser> band_users, int pagination) {

    }

    /**
     * @param from
     * @param radius
     */
    @Override
    public void fetchLiveBands(ai.portfolio.dev.project.app.com.padoi.Models.Location from, int radius) {

    }

    /**
     *
     */
    @Override
    public void fetchAllLiveBands() {

    }

    /**
     * ADDS current user to the PADOI database.
     *
     * @param fb_user
     */
    @Override
    public void createNewUser(FBUser fb_user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(PADOI.DBPATH_USERS + "/" + fb_user.getId());
        ref.setValue(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this.getApplicationContext(),"SUCCS LOG IN",Toast.LENGTH_LONG).show();
                           goToTrendingActivity(currentUser);
                        }else{
                            Log.e("ERR",task.toString());
                            Toast.makeText(MainActivity.this.getApplicationContext(),"FAILED LOG IN",Toast.LENGTH_LONG).show();
                        }
            }
        });
    }
}