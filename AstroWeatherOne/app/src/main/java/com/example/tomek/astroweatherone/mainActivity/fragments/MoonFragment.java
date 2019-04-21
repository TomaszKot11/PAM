package com.example.tomek.astroweatherone.mainActivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.mainActivity.MainActivity;
import com.example.tomek.astroweatherone.utilities.ProjectConstants;

public class MoonFragment extends Fragment implements MainActivity.SunMoonRefreshableUI {
    private TextView currentTimeTextView;
    private TextView moonRiseTextView;
    private TextView moonWaneTextView;
    private TextView moonNewMoonTextView;
    private TextView moonFullTextView;
    private TextView moonPhaseTextView;
    private TextView moonSynodicMonth;
    private TextView moonLongitute;

    private TextView moonLatitude;



    private OnFragmentInteractionListener mListener;

    public MoonFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoonFragment newInstance(String param1, String param2) {
        MoonFragment fragment = new MoonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public  void settingsRefreshUI(Bundle bundle) {
        moonLatitude.setText(bundle.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY));
        moonLongitute.setText(bundle.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentTimeTextView = getView().findViewById(R.id.moon_current_time_txt_view);
        moonRiseTextView = getView().findViewById(R.id.moon_rise_txt_view);
        moonWaneTextView = getView().findViewById(R.id.moon_wane_txt_view);
        moonNewMoonTextView = getView().findViewById(R.id.moon_new_moon_txt_view);
        moonFullTextView = getView().findViewById(R.id.moon_full_txt_view);
        moonPhaseTextView = getView().findViewById(R.id.moon_phase_moon_txt_view);
        moonSynodicMonth = getView().findViewById(R.id.moon_synodic_month_txt_view);
        moonLatitude = getView().findViewById(R.id.moon_latitude);
        moonLongitute = getView().findViewById(R.id.moon_longitude);

        moonLongitute.setText((getArguments().getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE)));
        moonLatitude.setText((getArguments().getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE)));
    }

    //TODO: refactor this
    @Override
    public void refreshUI(Bundle bundle, boolean isTimeUpdate, boolean isLongitudeLatitudeUpdate) {
//        Toast.makeText(getContext(), "MoonFramgnet", Toast.LENGTH_SHORT).show();
        if(isLongitudeLatitudeUpdate) {
           // mon
            moonLongitute.setText((bundle.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE)));
            moonLatitude.setText((bundle.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE)));
        }

        if(isTimeUpdate) {
            currentTimeTextView.setText(bundle.getString("DATE"));
        } else {

            moonRiseTextView.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_RISE_TIME, "NO DATA"));
            moonWaneTextView.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_SET_TIME, "NO DATA"));
            moonNewMoonTextView.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_NEW_MOON, "NO DATA"));
            moonFullTextView.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_FULL, "NO DATA"));
            //TODO: lines benath are propably wrong
            moonPhaseTextView.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_PHASE, "NO DATA"));
            moonSynodicMonth.setText(bundle.getString(ProjectConstants.BUNDLE_MOON_SYNODIC, "NO DATA"));


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moon, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }

        if(getActivity() instanceof  MainActivity) {
            ((MainActivity)getActivity()).addSubscriberFragment(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     *
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bundle bundle);
    }
}
