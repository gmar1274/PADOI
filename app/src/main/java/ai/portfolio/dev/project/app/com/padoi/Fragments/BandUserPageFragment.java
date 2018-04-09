package ai.portfolio.dev.project.app.com.padoi.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import ai.portfolio.dev.project.app.com.padoi.Interfaces.IBandAuth;
import ai.portfolio.dev.project.app.com.padoi.R;

import static ai.portfolio.dev.project.app.com.padoi.Activities.MainActivity.CLIENT_ID;
import static ai.portfolio.dev.project.app.com.padoi.Activities.MainActivity.REDIRECT_URI;
import static ai.portfolio.dev.project.app.com.padoi.Activities.MainActivity.REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BandUserPageFragment extends Fragment implements IBandAuth{


    private String isLINKED="BAND_VERIFIED";

    public BandUserPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean isLinked =false;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_band_user_page, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Button verifyBtn = (Button) rootView.findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    verifyBandUser();
            }
        });
        FrameLayout bandLay = (FrameLayout)rootView.findViewById(R.id.bandLayout);
        if(sharedPref.contains(isLINKED) && sharedPref.getBoolean(isLINKED,isLinked)){
            verifyBtn.setVisibility(View.GONE);
        }
        //SharedPreferences.Editor editor = sharedPref.edit();






        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void verifyBandUser() {

        // The only thing that's different is we added the 5 lines below.
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);
   }

}
