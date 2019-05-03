package com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.mainActivity.MainActivity;
import com.example.university.astroweathertwo.utilities.WeatherVisualizationFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class BasicWeatherInformationFragment extends Fragment implements MainActivity.ApiRequestObtainable {
    private TextView cityNameTextView;
    private TextView geographicalCoordinatesTextView;
    private TextView timeTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView descriptionTextView;
    private ImageView weatherConditionsVisualization;



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

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        View rootView = inflater.inflate(R.layout.fragment_basic_weather_information, container, false);
        rootView.setBackgroundColor(color);

        return rootView;
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
            String description =  currentObservationObject.getJSONObject("condition").getString("text");

            Integer weatherConditionsCode = currentObservationObject.getJSONObject("condition").getInt("code");

            // set text view texes
            cityNameTextView.setText(cityName);
            geographicalCoordinatesTextView.setText(geographicalCoordinates);
            timeTextView.setText(timeInformation);
            temperatureTextView.setText(temperatureInformation);
            pressureTextView.setText(pressureInformation);
            descriptionTextView.setText(description);
            WeatherVisualizationFactory.setBackgroundForImageView(weatherConditionsCode, weatherConditionsVisualization, getActivity());
        } catch(JSONException | NullPointerException | IOException e) {
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
       descriptionTextView = getView().findViewById(R.id.description_text_view);
       weatherConditionsVisualization  = getView().findViewById(R.id.weather_visualization_image_view);

        if(getActivity() instanceof MainActivity) {
            updateUIWithInfoFromApi(((MainActivity)getActivity()).getJsonObject());
            ((MainActivity)getActivity()).addSubscribeApiListener(this);
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
