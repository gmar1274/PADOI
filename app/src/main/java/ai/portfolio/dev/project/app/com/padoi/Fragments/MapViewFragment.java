package ai.portfolio.dev.project.app.com.padoi.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTaskLoaders.LiveBandNearbyLoader;
import ai.portfolio.dev.project.app.com.padoi.Interfaces.IMyAPI;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.Utils.PADOI;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback, IMyAPI {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Location mUserLoc;
    private List<BandUser> bandList;
    private LiveBandNearbyLoader mAdapter;
    private int mOffest;//API offset

    public MapViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapViewFragment newInstance(String param1, String param2) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //currently no view to attach the datasource to ....
        mAdapter = new LiveBandNearbyLoader(this.getContext(), mUserLoc);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }/* else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onMapReady(GoogleMap googleMap) {
            // Add a marker in Sydney, Australia,
            // and move the map's camera to the same location.
            LatLng defualt = new LatLng(-33.852, 151.211);
            if(mUserLoc !=null)defualt = new LatLng(mUserLoc.getLatitude(), mUserLoc.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(defualt)
                    .title("Me").alpha(.5f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defualt,15));//15 is street view and 20 is buildings
            // need to apply a API request here to fetch live entertainment. needs a limit
           DEBUG(googleMap, bandList);

    }

    private void DEBUG(GoogleMap googleMap, List<BandUser> bandList) {
        for(BandUser b : bandList){
            BandUser band =b;
            band.getNearDistDEBUG(mUserLoc);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(PADOI.DBPATH_BAND_USERS);//query bandusers
            GeoFire geoFire = new GeoFire(ref);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(band.getLocation().getLatitude(),band.getLocation().getLongitude())).title(band.getName()));
        }
    }

    /**
     * Will fetch from firebase a list of Live bands near user Location
     *
     * @param userLoc user geolocation
     * @return
     */
    @Override
    public LoaderManager.LoaderCallbacks<List<BandUser>> getNearbyLiveEvents(final Location userLoc) {
        return new LoaderManager.LoaderCallbacks<List<BandUser>>() {
            @Override
            public Loader<List<BandUser>> onCreateLoader(int id, Bundle args) {
                return new LiveBandNearbyLoader(MapViewFragment.this.getContext(),userLoc);
            }

            @Override
            public void onLoadFinished(Loader<List<BandUser>> loader, List<BandUser> data) {
                mAdapter.swap(data);
            }

            @Override
            public void onLoaderReset(Loader<List<BandUser>> loader) {
                mAdapter.empty();
            }
        };
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * From MainActivity
     * @param userLocation
     */
    public Fragment setLocation(Location userLocation){
        this.mUserLoc = userLocation;
        return this;
    }
    public Fragment setBandList(List<BandUser> list){
        this.bandList = new ArrayList<>(list);
        return this;
    }
}
