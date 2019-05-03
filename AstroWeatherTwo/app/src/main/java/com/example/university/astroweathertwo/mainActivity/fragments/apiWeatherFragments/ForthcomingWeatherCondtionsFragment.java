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
    private List<TableRow> listViews = new LinkedList<>();
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
        return inflater.inflate(R.layout.fragment_forthcoming_weather_condtions, container, false);
    }


    @Override
    public void refreshUI(JSONObject jsonObject) {
        if(listViews.size() == NUMBER_OF_FORTHCOMMING_WEATHER_LIST_ELEMENTS)
            refreshAllListElements(jsonObject);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity currentActivity = getActivity();
        if(currentActivity instanceof MainActivity)
            ((MainActivity) currentActivity).addSubscribeApiListener(this);


        Random random = new Random();


        //TODO: this could be written better
        listViews.add(getView().findViewById(R.id.table_row_0));
        listViews.add(getView().findViewById(R.id.table_row_1));
        listViews.add(getView().findViewById(R.id.table_row_2));
        listViews.add(getView().findViewById(R.id.table_row_3));
        listViews.add(getView().findViewById(R.id.table_row_4));
        listViews.add(getView().findViewById(R.id.table_row_5));
        listViews.add(getView().findViewById(R.id.table_row_6));
        listViews.add(getView().findViewById(R.id.table_row_7));
        listViews.add(getView().findViewById(R.id.table_row_8));
        listViews.add(getView().findViewById(R.id.table_row_9));


        for(int i = 0; i < listViews.size(); i++)
            listViews.get(i).setBackgroundColor(getRandomColor(random));


    }

    private int getRandomColor(Random rnd) {
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void refreshAllListElements(JSONObject jsonObject) {
        try {
            for (int i = 0; i < NUMBER_OF_FORTHCOMMING_WEATHER_LIST_ELEMENTS; i++)
                refreshSingleListElementUI(jsonObject, i, listViews.get(i));
        } catch(JSONException e) {
            Toast.makeText(getContext(), "Error while updating forthcomming weather list!", Toast.LENGTH_LONG).show();
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
