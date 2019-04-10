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

public class SunFragment extends Fragment implements MainActivity.SunMoonRefreshableUI{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView currentTimeTextView;
    private TextView sunRiseTextView;
    private TextView sunRiseAzimuthTextView;
    private TextView sunSetTextView;
    private TextView sunSunsetAzimuthTextView;
    private TextView sunCivilMorningTwilightTextView;
    private TextView sunCivilEveningTwilightTextView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SunFragment() {
        // Required empty public constructor

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        MainActivity mainActivity = args.getParcelable("mainActivity");
        if(mainActivity != null) {
            mainActivity.addSubscriberFragment(this);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SunFragment newInstance(String param1, String param2) {
        SunFragment fragment = new SunFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentTimeTextView = getView().findViewById(R.id.sun_current_time_txt_view);
        sunRiseTextView = getView().findViewById(R.id.sun_rise_txt_view);
        sunRiseAzimuthTextView = getView().findViewById(R.id.sun_sunrise_azimuth_txt_view);
        sunSetTextView = getView().findViewById(R.id.sun_sunset_txt_view);
        sunSunsetAzimuthTextView = getView().findViewById(R.id.sun_sunset_azimuth_txt_view);
        sunCivilMorningTwilightTextView = getView().findViewById(R.id.sun_civil_morning_twilight_txt_view);
        sunCivilEveningTwilightTextView = getView().findViewById(R.id.sun_civil_evening_twilight_txt_view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sun, container, false);
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


    @Override
    public void refreshUI(Bundle bundle, boolean isTimeupdate) {
//        Toast.makeText(getContext(), "SunFramgnet", Toast.LENGTH_SHORT).show();
        if(isTimeupdate) {
            currentTimeTextView.setText(bundle.getString("DATE"));
        } else {
            sunRiseTextView.setText(bundle.getString("SUN_RISE_TIME", "NO DATA"));
            sunRiseAzimuthTextView.setText(bundle.getString("SUN_RISE_AZIMUTH", "NO DATA"));
            sunSetTextView.setText(bundle.getString("SUN_SET_TIME", "NO DATA"));
            sunSunsetAzimuthTextView.setText(bundle.getString("SUN_SET_AZIMUTH", "NO DATA"));
            sunCivilEveningTwilightTextView.setText(bundle.getString("SUN_CIVIL_EVENING_TWILIGHT", "NO DATA"));
            sunCivilMorningTwilightTextView.setText(bundle.getString("SUN_CIVIL_MORNING_TWILIGHT", "NO DATA"));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Bundle bundle);
    }
}
