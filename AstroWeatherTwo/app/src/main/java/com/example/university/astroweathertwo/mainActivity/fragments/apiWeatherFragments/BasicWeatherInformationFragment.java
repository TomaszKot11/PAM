package com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.mainActivity.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class BasicWeatherInformationFragment extends Fragment implements MainActivity.ApiRequestObtainable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView cityNameTextView;
    private TextView geographicalCoordinatesTextView;
    private TextView timeTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView;


    private OnFragmentInteractionListener mListener;

    public BasicWeatherInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicWeatherInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicWeatherInformationFragment newInstance(String param1, String param2) {
        BasicWeatherInformationFragment fragment = new BasicWeatherInformationFragment();
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
        return inflater.inflate(R.layout.fragment_basic_weather_information, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    //TODO: this is propably to be removed
    @Override
    public void refreshUI(JSONObject jsonObject) {
            updateUIWithInfoFromApi(jsonObject);
    }


    private void updateUIWithInfoFromApi(JSONObject jsonObject) {

        try {
            //decompose JSON
            JSONObject locationObject = jsonObject.getJSONObject("location");
            String cityName = locationObject.getString("city");
            String geographicalCoordinates = locationObject.getDouble("lat") + " " + locationObject.getDouble("long");
            String timeInformation = locationObject.getString("timezone_id");

            JSONObject currentObservationObject = jsonObject.getJSONObject("current_observation");
            String temperatureInformation = String.valueOf(currentObservationObject.getJSONObject("condition").getDouble("temperature"));
            String pressureInformation = String.valueOf(currentObservationObject.getJSONObject("atmosphere").getDouble("pressure"));

            // set text view texes
            cityNameTextView.setText(cityName);
            geographicalCoordinatesTextView.setText(geographicalCoordinates);
            timeTextView.setText(timeInformation);
            temperatureTextView.setText(temperatureInformation);
            pressureTextView.setText(pressureInformation);
        } catch(JSONException | NullPointerException e) {
            //TODO: refactor this
            Log.e("BasicWeatherInformation", e.toString());
            Log.d("BasicWeatherInformation", Log.getStackTraceString(e));
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       cityNameTextView = getView().findViewById(R.id.city_name_text_view);
       geographicalCoordinatesTextView = getView().findViewById(R.id.geographical_coordiantes_text_view);
       timeTextView = getView().findViewById(R.id.time_text_view);
       temperatureTextView = getView().findViewById(R.id.temperature_text_view);
       pressureTextView = getView().findViewById(R.id.pressure_text_view);

        if(getActivity() instanceof MainActivity)
            updateUIWithInfoFromApi(((MainActivity)getActivity()).getJsonObject());


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
