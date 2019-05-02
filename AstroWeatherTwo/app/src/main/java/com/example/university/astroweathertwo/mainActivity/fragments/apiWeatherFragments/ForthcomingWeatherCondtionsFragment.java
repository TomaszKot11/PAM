package com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.university.astroweathertwo.R;

public class ForthcomingWeatherCondtionsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    // required empty constructor
    public ForthcomingWeatherCondtionsFragment() { }

    public static ForthcomingWeatherCondtionsFragment newInstance(String param1, String param2) {
        ForthcomingWeatherCondtionsFragment fragment = new ForthcomingWeatherCondtionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView rootView = (ScrollView)inflater.inflate(R.layout.fragment_forthcoming_weather_condtions, container, false);

        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.scroll_view_forthcomming_weather_linear_layout);

        for(int i = 0 ; i < 10 ; i++) {
            View view = inflater.inflate(R.layout.forthcoming_weather_tile,null);

//            TextView textView = view.findViewById(R.id.forthcomming_temperature_text_view);

            linearLayout.addView(view);
        }

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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
