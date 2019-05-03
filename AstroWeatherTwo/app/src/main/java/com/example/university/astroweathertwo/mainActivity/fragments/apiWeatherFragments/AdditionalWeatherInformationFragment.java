package com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.mainActivity.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AdditionalWeatherInformationFragment extends Fragment implements MainActivity.ApiRequestObtainable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView windStrengthTextView;
    private TextView windDirectionTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;

    private OnFragmentInteractionListener mListener;

    public AdditionalWeatherInformationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdditionalWeatherInformationFragment newInstance(String param1, String param2) {
        AdditionalWeatherInformationFragment fragment = new AdditionalWeatherInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        windStrengthTextView = getView().findViewById(R.id.wind_strength_text_view);
        windDirectionTextView = getView().findViewById(R.id.wind_direction_text_view);
        humidityTextView = getView().findViewById(R.id.humidity_text_view);
        visibilityTextView = getView().findViewById(R.id.visibility_text_view);

        Activity curretnAcitvity = getActivity();

        if(curretnAcitvity instanceof MainActivity) {
            updateUIWithDataFromApi(((MainActivity) curretnAcitvity).getJsonObject());
            ((MainActivity) curretnAcitvity).addSubscribeApiListener(this);
        }
    }

    private void updateUIWithDataFromApi(JSONObject jsonObject) {
        try {
            JSONObject windObject = jsonObject.getJSONObject("current_observation").getJSONObject("wind");
            String windStrength = String.valueOf(windObject.getDouble("speed"));
            String windDirection = String.valueOf(windObject.getDouble("direction"));

            JSONObject atmosphereObject = jsonObject.getJSONObject("current_observation").getJSONObject("atmosphere");
            String humidity = String.valueOf(atmosphereObject.getInt("humidity"));
            String visiblity = String.valueOf(atmosphereObject.getDouble("visibility"));

            // set the labels
            windStrengthTextView.setText(windStrength);
            windDirectionTextView.setText(windDirection);
            humidityTextView.setText(humidity);
            visibilityTextView.setText(visiblity);

        } catch(JSONException | NullPointerException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
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

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        View rootView = inflater.inflate(R.layout.fragment_additional_weather_information, container, false);
        rootView.setBackgroundColor(color);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void refreshUI(JSONObject jsonObject) {
        updateUIWithDataFromApi(jsonObject);
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
