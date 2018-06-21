package ai.portfolio.dev.project.app.com.padoi.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTaskLoaders.LocationLoader;
import ai.portfolio.dev.project.app.com.padoi.AsyncTasks.DownLoadImageTask;
import ai.portfolio.dev.project.app.com.padoi.Fragments.BandUserPageFragment;
import ai.portfolio.dev.project.app.com.padoi.Fragments.MapViewFragment;
import ai.portfolio.dev.project.app.com.padoi.Fragments.TrendingFragment;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.IPADOIFragments;
import ai.portfolio.dev.project.app.com.padoi.Models.FBUser;
import ai.portfolio.dev.project.app.com.padoi.Models.PADOIUser;
import ai.portfolio.dev.project.app.com.padoi.R;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , IPADOIFragments
{

    private static final String TAG = "FIREBASE TAG";
    private static final int TAG_CODE_PERMISSION_LOCATION = 100;
    private PADOIUser mCurrentUser; // data from PADOI like likes friends etc...
    private FBUser mFbUser;//data from fb graph API

    private Location mGpsLocation;
    private LoaderManager.LoaderCallbacks<Location> loaderCallbacks = new LoaderManager.LoaderCallbacks<Location>() {
        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {

            return new LocationLoader(MainActivity.this.getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {
            mGpsLocation = data;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fetchPADOIUser(mFbUser, mGpsLocation);
                }
            }).start();
            //Toast.makeText(MainActivity.this.getApplicationContext(),"Location: "+data,Toast.LENGTH_LONG).show();
            //make BAND REQUEST from GPS nearby
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {
        }
    };

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportLoaderManager().initLoader(R.string.LocationLoader, null, loaderCallbacks);

        String name = null, image_url = null, id = null;
        try {
            ///////////////////Get Extras
            Bundle login_bundle = getIntent().getExtras();
            name = login_bundle.getString("name").toString();
            image_url = login_bundle.getString("image_url");
            id = login_bundle.getString("id");
            mFbUser = new FBUser(id, image_url, name);
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
                    requestUserLocation();
                } else {//user did not grant permission
                    Toast.makeText(this, "Permission to access GPS was denied. :(", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    /**
     * @param headerView - from navigation View
     * @param rootView   - from root view
     */
    private void customGUISettings(View headerView, View rootView) {
        displayFragmentScreen(R.id.trending_menu_item);
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

    /**
     * Navigation onNavigationItemSelected listener implementation.
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.fb_log_out_id) {
            logOut();
        } else {
            displayFragmentScreen(id);//displays a certain fragment user selected
        }
        return true;
    }

    /**
     * Switch on id from the menu option method to display a new functionality (fragment) on mainActivity screen.
     *
     * @param id
     * @return
     */
    public void displayFragmentScreen(int id) {
        Fragment frag = null;
        switch (id) {
            case R.id.mapView_menu_item:
                frag = mapViewFragment();
                break;
            case R.id.trending_menu_item:
                if(mGpsLocation !=null) {
                  frag = trendingFragment();
                }
                break;
            case R.id.band_user_frag_menu_item:
                frag = bandUserPage();
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
        FirebaseAuth.getInstance().signOut();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    private void fetchPADOIUser(FBUser user, Location loc){
        //mCurrentUser = new PADOIUser(user.getId(),loc);
        displayFragmentScreen(R.id.trending_menu_item);
    }
    //////////////////////////////////
    public Fragment getCurrentFragmentVisible() {
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        //Log.d("LIST:::::",allFragments+"");
        if (allFragments == null || allFragments.isEmpty()) {
            return null;
        }
        return allFragments.get(0);
    }

    @Override
    public Fragment trendingFragment() {
        TrendingFragment trend = new TrendingFragment();
        trend.setLocation(mGpsLocation);
        trend.setUser(mFbUser);
        return  trend;
    }

    @Override
    public Fragment mapViewFragment() {
        MapViewFragment map = new MapViewFragment();
        TrendingFragment t = (TrendingFragment) getCurrentFragmentVisible();
        //Log.d("TREEEEEEEEEE",(t==null)+"......"+t+".....data:: "+t.getBandList());
        if(t!=null){
            map.setBandList(t.getBandList());
        }
        map.setLocation(mGpsLocation);
        return map;
    }

    @Override
    public Fragment bandUserPage() {
        BandUserPageFragment bandUserPageFragment = new BandUserPageFragment();
        return  bandUserPageFragment;
    }


    public static final int REQUEST_CODE=1337;
    // TODO: Replace with your client ID
    public static final String CLIENT_ID = "9fb00fda297a47e193f9f61b6d570f0f";

    // TODO: Replace with your redirect URI
    public static final String REDIRECT_URI = "https://padoi-196001.firebaseio.com/";//"https://open.spotify.com/user/";//"yourcustomprotocol://callback";
    /**********
     *
     */
    /**
     * When requestCode equals REQUEST_CODE: then Spotify api has been called. Figure out if JSON response was valid or not.
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //Log.d("HERRRRRR: ","HERRRR: REQUEST: "+resultCode +" .. "+requestCode);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
          //  Log.d("SPOTIFY TAG:::::","TOKEN:: "+response.getAccessToken()+" .... "+response+" ...CODE..."+response.getCode());
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Toast.makeText(MainActivity.this.getApplicationContext(),"Spotify account is linked!!",Toast.LENGTH_LONG).show();
                    BandUserPageFragment frag= (BandUserPageFragment) getCurrentFragmentVisible();
                    frag.spotifyAccessTokenVerified(response);
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    Toast.makeText(this,"Spotify error. :(",Toast.LENGTH_LONG).show();
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}