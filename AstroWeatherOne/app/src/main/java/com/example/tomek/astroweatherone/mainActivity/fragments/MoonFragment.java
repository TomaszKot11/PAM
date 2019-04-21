package com.example.tomek.astroweatherone.mainActivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.mainActivity.MainActivity;
import com.example.tomek.astroweatherone.utilities.StringConstants;

public class MoonFragment extends Fragment implements MainActivity.SunMoonRefreshableUI {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView currentTimeTextView;
    private TextView moonRiseTextView;
    private TextView moonWaneTextView;
    private TextView moonNewMoonTextView;
    private TextView moonFullTextView;
    private TextView moonPhaseTextView;
    private TextView moonSynodicMonth;
    private TextView moonLongitute;

    private TextView moonLatitude;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public  void settingsRefreshUI(Bundle bundle) {
        moonLatitude.setText(bundle.getString(StringConstants.PREFERENCE_LATITTUDE_KEY));
        moonLongitute.setText(bundle.getString(StringConstants.PREFERENCES_LONGITUTDE_KEY));
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

        moonLongitute.setText((getArguments().getString(StringConstants.PREFERENCES_LONGITUTDE_KEY, StringConstants.DMCS_LONGITUDE)));
        moonLatitude.setText((getArguments().getString(StringConstants.PREFERENCE_LATITTUDE_KEY, StringConstants.DMCS_LATITUDE)));
    }

    //TODO: refactor this
    @Override
    public void refreshUI(Bundle bundle, boolean isTimeUpdate, boolean isLongitudeLatitudeUpdate) {
//        Toast.makeText(getContext(), "MoonFramgnet", Toast.LENGTH_SHORT).show();
        if(isLongitudeLatitudeUpdate) {
           // mon
            moonLongitute.setText((bundle.getString(StringConstants.PREFERENCES_LONGITUTDE_KEY, StringConstants.DMCS_LONGITUDE)));
            moonLatitude.setText((bundle.getString(StringConstants.PREFERENCE_LATITTUDE_KEY, StringConstants.DMCS_LATITUDE)));
        }

        if(isTimeUpdate) {
            currentTimeTextView.setText(bundle.getString("DATE"));
        } else {

            moonRiseTextView.setText(bundle.getString(StringConstants.BUNDLE_MOON_RISE_TIME, "NO DATA"));
            moonWaneTextView.setText(bundle.getString(StringConstants.BUNDLE_MOON_SET_TIME, "NO DATA"));
            moonNewMoonTextView.setText(bundle.getString(StringConstants.BUNDLE_MOON_NEW_MOON, "NO DATA"));
            moonFullTextView.setText(bundle.getString(StringConstants.BUNDLE_MOON_FULL, "NO DATA"));
            //TODO: lines benath are propably wrong
            moonPhaseTextView.setText(bundle.getString(StringConstants.BUNDLE_MOON_PHASE, "NO DATA"));
            moonSynodicMonth.setText(bundle.getString(StringConstants.BUNDLE_MOON_SYNODIC, "NO DATA"));


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
