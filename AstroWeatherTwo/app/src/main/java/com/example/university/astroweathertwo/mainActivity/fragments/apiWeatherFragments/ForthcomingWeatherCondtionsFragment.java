package com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.mainActivity.MainActivity;
import com.example.university.astroweathertwo.utilities.WeatherVisualizationFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ForthcomingWeatherCondtionsFragment extends Fragment implements MainActivity.ApiRequestObtainable {


    private OnFragmentInteractionListener mListener;
    private List<View> listViews = new LinkedList<>();
    public static final int NUMBER_OF_FORTHCOMMING_WEATHER_LIST_ELEMENTS = 10;
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

//        listViews.clear();
        ScrollView rootView = (ScrollView)inflater.inflate(R.layout.fragment_forthcoming_weather_condtions, container, false);

        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.scroll_view_forthcomming_weather_linear_layout);
        Activity currentActivity = getActivity();
        JSONObject jsonObject = currentActivity instanceof MainActivity ? ((MainActivity)currentActivity).getJsonObject() : null;

        for(int i = 0 ; i < NUMBER_OF_FORTHCOMMING_WEATHER_LIST_ELEMENTS ; i++) {
            View view = inflater.inflate(R.layout.forthcoming_weather_tile,null);

           Random rnd = new Random();
           int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            view.setBackgroundColor(color);
            try {
                if (jsonObject != null) refreshSingleListElementUI(jsonObject, i, view);
            } catch(JSONException e) {
                Log.e("ForthcommingWeather", e.toString());
                Log.d("ForthcommingWeather", Log.getStackTraceString(e));
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

            linearLayout.addView(view);
        }

        return rootView;
    }


    @Override
    public void refreshUI(JSONObject jsonObject) {
//        refreshAllListElementUI(jsonObject);
        //replace the fragment forces to call the onCreateView
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity currentActivity = getActivity();
        if(currentActivity instanceof MainActivity) {
            ((MainActivity) currentActivity).addSubscribeApiListener(this);
            JSONObject jsonObject = ((MainActivity)currentActivity).getJsonObject();
        }
    }

    private void refreshSingleListElementUI(JSONObject jsonObject, int index, View listViewElement) throws JSONException {
        JSONArray forthcommingWeatherArray = jsonObject.getJSONArray("forecasts");
        JSONObject singleForthcommingJsonObject = forthcommingWeatherArray.getJSONObject(index);

        TextView forthcommingDayNameTextView = listViewElement.findViewById(R.id.forthcomming_day_name_text_view);
        TextView forthcommingLowTemperatureTextView = listViewElement.findViewById(R.id.forthcomming_low_temperature_text_view);
        TextView forthcommingHighTemperatureTextView = listViewElement.findViewById(R.id.forthcomming_high_temperature_text_view);
        ImageView forthcommingWeatherVisualizationImageView = listViewElement.findViewById(R.id.forthcomming_weather_visualization);


        // get appropriate values from JSON
        String temperatureLow = String.valueOf(singleForthcommingJsonObject.getDouble("low"));
        String temperatureHigh = String.valueOf(singleForthcommingJsonObject.getDouble("high"));
        int weatherConditionsCode = singleForthcommingJsonObject.getInt("code");
        String dayName =  singleForthcommingJsonObject.getString("day");


        // updateViews views
        forthcommingDayNameTextView.setText(dayName);
        forthcommingLowTemperatureTextView.setText(temperatureLow);
        forthcommingHighTemperatureTextView.setText(temperatureHigh);
        //forthcommingWeatherVisualizationTextView.setBackground();

        try {
            WeatherVisualizationFactory.setBackgroundForImageView(weatherConditionsCode, forthcommingWeatherVisualizationImageView, getActivity());
        } catch(IOException e) {
            Log.e("ForthcommingWeather", e.toString());
            Log.d("ForthcommingWeather", Log.getStackTraceString(e));
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
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
