package ai.portfolio.dev.project.app.com.padoi.Fragments;


import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ai.portfolio.dev.project.app.com.padoi.AsyncTaskLoaders.BandUsersLoader;
import ai.portfolio.dev.project.app.com.padoi.Models.BandUser;
import ai.portfolio.dev.project.app.com.padoi.Models.PadoiUser;
import ai.portfolio.dev.project.app.com.padoi.R;
import ai.portfolio.dev.project.app.com.padoi.RecycleViewAdpaters.BandAdapterRV;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrendingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Location userLoc;
    private PadoiUser mUser;
    //private BandUserAdapter adapter;


    private OnFragmentInteractionListener mListener;
    private BandAdapterRV adapter;

    public TrendingFragment() {}

    public Fragment setUser(PadoiUser user) {
        this.mUser = user;
        return this;
    }
    public Fragment setLocation(Location loc){
        this.userLoc = loc;
        return this;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
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
        //adapter = new BandUserAdapter(this.getContext(), R.layout.trending_layout);
        View rootView = inflater.inflate(R.layout.fragment_trending, container, false);

        final ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.trendingProgressBar);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycle_view_BANDS);
        adapter = new BandAdapterRV(mUser,null, this.getContext(), R.layout.trending_layout);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(horizontalLayoutManager);
        rv.setAdapter(adapter);


        LoaderManager.LoaderCallbacks<List<BandUser>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<BandUser>>() {
            @Override
            public Loader<List<BandUser>> onCreateLoader(int id, Bundle args) {
                return new BandUsersLoader(TrendingFragment.this.getContext());
            }

            @Override
            public void onLoadFinished(Loader<List<BandUser>> loader, List<BandUser> data) {
                pb.setVisibility(View.GONE);
                adapter.add(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<List<BandUser>> loader) {
                adapter.clear();
            }
        };
        this.getLoaderManager().initLoader(R.string.TrendingLoading, null, loaderCallbacks);

        return rootView;
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
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public List<BandUser> getBandList() {
        return this.adapter.getList();
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
}
