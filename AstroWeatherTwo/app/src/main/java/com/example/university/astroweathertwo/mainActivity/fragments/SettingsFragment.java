package com.example.university.astroweathertwo.mainActivity.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.university.astroweathertwo.CitiesListActivity;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.SettingsActivity;
import com.example.university.astroweathertwo.mainActivity.MainActivity;
import com.example.university.astroweathertwo.utilities.ProjectConstants;
import com.example.university.astroweathertwo.utilities.Settings;
import com.example.university.astroweathertwo.utilities.SharedPreferencesUtility;
import com.example.university.astroweathertwo.utilities.api.ApiRequest;
import com.example.university.astroweathertwo.utilities.api.ApiRequester;
import com.example.university.astroweathertwo.utilities.database.SQLiteDatabaseHelper;
import com.example.university.astroweathertwo.utilities.database.entities.City;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Spinner spinnerUnits;
    private ArrayAdapter<CharSequence> spinnerUnitsArrayAdapter;
    private Spinner spinnerValue;
    private ArrayAdapter<CharSequence> spinnerValueArrayAdapter;
    private Spinner spinnerWeatherLocalization;
    private ArrayAdapter<String> spinnerWeatherLocalizationAdapter;
    // to avoid triggering event watching on edit text when
    // text is set programmatically
    private boolean canWatch = false;
    private TextInputEditText latitudeEditText;
    private TextInputEditText longitutdeEditText;
    private TextInputEditText favouriteCityEditText;

    private Button favouriteAddCityButton;
    private Button showAllCitiesButton;
    private Button forceRefreshButton;

    private String previousLatitudeValue;
    private String previousLongitudeValue;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    public void subscribeListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        spinnerValue = getView().findViewById(R.id.time_value_spinner);
        spinnerUnits = getView().findViewById(R.id.time_unit_spinner);
        spinnerWeatherLocalization = getView().findViewById(R.id.weather_localization_spinner);

        latitudeEditText = getView().findViewById(R.id.latitudeInputEditText);
        longitutdeEditText = getView().findViewById(R.id.longitudeInputEditText);
        favouriteCityEditText = getView().findViewById(R.id.favourite_city_input_edit_text);

        this.spinnerValueArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_numerical_values, android.R.layout.simple_spinner_item);
        this.spinnerUnitsArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_units, android.R.layout.simple_spinner_item);

        updateCitySpinner();

        assingCharAdaptersToSpinners(spinnerValue, spinnerValueArrayAdapter);
        assingCharAdaptersToSpinners(spinnerUnits, spinnerUnitsArrayAdapter);

        this.previousLatitudeValue = String.valueOf(SharedPreferencesUtility.getLatitude(getActivity()));
        this.previousLongitudeValue = String.valueOf(SharedPreferencesUtility.getLongitude(getActivity()));

        favouriteAddCityButton = getView().findViewById(R.id.button_add_favourite_city);
        showAllCitiesButton = getView().findViewById(R.id.button_see_all_cities);
        forceRefreshButton = getView().findViewById(R.id.btn_refresh_data);

        configureActionListenersForControls();

        configureSaveToDatabaseButtonClick();

        configureShowAllCitiesClick();

        configureForceRefreshDataClick();
    }

    private ArrayList<String> getLocalizationFromDatabase() {
        SQLiteDatabaseHelper sqLiteDatabaseHelper = SQLiteDatabaseHelper.getInstance(getActivity());

       List<City> cities =  sqLiteDatabaseHelper.allCities();
       ArrayList<String> charSequencesList = new ArrayList<>();

       for(City city : cities) {
            charSequencesList.add(city.getLocationString());
       }

       return charSequencesList;
    }



    private void configureForceRefreshDataClick() {
        forceRefreshButton.setOnClickListener(v -> {
            Activity activity = SettingsFragment.this.getActivity();

            if(activity instanceof MainActivity)
                ((MainActivity)activity).sendWeatherApiRequest();

        });
    }

    private void configureShowAllCitiesClick() {
        showAllCitiesButton.setOnClickListener(v -> {
            if(SettingsFragment.this.getActivity() instanceof SettingsActivity) {
                Intent intent = new Intent(getActivity(), CitiesListActivity.class);
                startActivity(intent);
            } else if(SettingsFragment.this.getActivity() instanceof  MainActivity) {
                SettingsFragment.this.mListener.citiesListClicked();
            }
        });

    }


    private void configureSaveToDatabaseButtonClick() {
        this.favouriteAddCityButton.setOnClickListener(v -> SettingsFragment.this.performSavingFavouriteCity());
    }


    private void performSavingFavouriteCity() {
        //TODO: in the background?
        //TODO: avoid duplication of localizations
        String location = favouriteCityEditText.getText().toString();

        if(!location.matches("[A-Za-z]+,[A-Za-z]+")) {
            Toast.makeText(getActivity(), "Not valid location", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiRequester requestManager = ApiRequester.getInstance(getActivity());

        ApiRequest request = new ApiRequest(Request.Method.GET, null, null, location,  new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    int woeid = ((JSONObject) response).getJSONObject("location").getInt("woeid");

                    Log.e("Response", ((JSONObject)response).toString());
                    Log.e(location, String.valueOf(woeid));

                    String[] cityCode = location.split(",");

                    City city = new City(cityCode[0], cityCode[1], String.valueOf(woeid), location);
                    SQLiteDatabaseHelper sqLiteDatabaseHelper = SQLiteDatabaseHelper.getInstance(getActivity());
                    sqLiteDatabaseHelper.addCity(city);

                    SettingsFragment.this.spinnerWeatherLocalizationAdapter.add(city.getLocationString());
                    SettingsFragment.this.spinnerWeatherLocalizationAdapter.notifyDataSetChanged();

                    Toast.makeText(SettingsFragment.this.getActivity(), "Favourite city saved!", Toast.LENGTH_LONG).show();
                } catch(JSONException e) {
                    Toast.makeText(getActivity(), "Not valid location", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Add error handling here
                Log.e("API error: ", "Error while woeid validatiion API call in SettingsFragment");
                Toast.makeText(getActivity(), "Not valid location", Toast.LENGTH_SHORT).show();;
            }
        });

        requestManager.addToRequestQueue(request);
    }

    private void assingCharAdaptersToSpinners(Spinner spinner, ArrayAdapter<CharSequence> arrayAdapter) {
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
    }

    private void configureActionListenersForControls() {
        latitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = latitudeEditText.getText().toString();

                if(newValue.length() > 0) {

                    double value = Double.parseDouble(newValue);
                    boolean validationResult = validateEditTextRange(ProjectConstants.LATITUDE_MIN, ProjectConstants.LATITUDE_MAX, value);

                    if(!validationResult)
                        latitudeEditText.setText(SettingsFragment.this.previousLatitudeValue);

                    if (SettingsFragment.this.mListener != null && canWatch && validationResult) {
                        SettingsFragment.this.previousLatitudeValue = latitudeEditText.getText().toString();
                        Settings settings = SettingsFragment.this.produceSettingsFromControls();
                        SettingsFragment.this.mListener.onFragmentInteraction(settings);
                    }
                }
            }
        });

        longitutdeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = longitutdeEditText.getText().toString();

                if(newValue.length() > 0) {
                    boolean validationResult = validateEditTextRange(ProjectConstants.LONGITUDE_MIN, ProjectConstants.LONGITUDE_MAX, Double.parseDouble(newValue));

                    if(!validationResult)
                        longitutdeEditText.setText(SettingsFragment.this.previousLongitudeValue);

                    if (SettingsFragment.this.mListener != null && canWatch && validationResult) {
                        SettingsFragment.this.previousLongitudeValue = longitutdeEditText.getText().toString();
                        Settings settings = SettingsFragment.this.produceSettingsFromControls();
                        SettingsFragment.this.mListener.onFragmentInteraction(settings);
                    }
                }
            }
        });

        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(SettingsFragment.this.mListener != null) {
                    Settings settings = SettingsFragment.this.produceSettingsFromControls();
                    SettingsFragment.this.mListener.onFragmentInteraction(settings);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(SettingsFragment.this.mListener != null) {
                    Settings settings = SettingsFragment.this.produceSettingsFromControls();
                    SettingsFragment.this.mListener.onFragmentInteraction(settings);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerWeatherLocalization.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(SettingsFragment.this.mListener != null) {
                            Settings settings = SettingsFragment.this.produceSettingsFromControls();
                            SettingsFragment.this.mListener.onFragmentInteraction(settings);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );
    }


    private boolean validateEditTextRange(double min, double max, double incommingValue) {
        if(!canWatch) return true;

        if(incommingValue <= max && incommingValue >= min)
            return true;

        Toast.makeText(getActivity(), String.format("The value should be between %.1f and %.1f", min, max), Toast.LENGTH_LONG).show();
        return  false;
    }



    private Settings produceSettingsFromControls() {
        Settings settings;
        try {
            String localizationString = "lodz,pl";
            if(spinnerWeatherLocalization.getSelectedItem() != null)
                localizationString = spinnerWeatherLocalization.getSelectedItem().toString();


            settings = new Settings(Double.parseDouble(longitutdeEditText.getText().toString()),
                    Double.parseDouble(latitudeEditText.getText().toString()),
                    Integer.parseInt(spinnerValue.getSelectedItem().toString()),
                    spinnerUnits.getSelectedItem().toString(),
                    localizationString);

            return settings;
        } catch(NumberFormatException e) {
            Log.e("[SettingsFragment]", "NumberFormatException in SettingsFragment.");
            Log.e("[SettingsFragment]", e.toString());
        }

        return new Settings(Double.parseDouble(ProjectConstants.DMCS_LONGITUDE), Double.parseDouble(ProjectConstants.DMCS_LATITUDE), 5, "seconds", "lodz,pl");
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences settings = getActivity().getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);

        int position = spinnerUnitsArrayAdapter.getPosition(settings.getString(ProjectConstants.PREFERNCE_TIME_UNIT_KEY, "seconds"));
        spinnerUnits.setSelection(position);

        position = spinnerValueArrayAdapter.getPosition(settings.getString(ProjectConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        spinnerValue.setSelection(position);

        String weatherLocalization = settings.getString(ProjectConstants.PREFERENCES_LOCALIZATION_STRING_KEY, "NONE");
        if(!weatherLocalization.equals("NONE")) {
            position = spinnerWeatherLocalizationAdapter.getPosition(weatherLocalization);
            spinnerWeatherLocalization.setSelection(position);
        }

        String value = settings.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE);
        latitudeEditText.setText(value);

        value = settings.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE);
        longitutdeEditText.setText(value);
    }

    @Override
    public void onResume() {
        super.onResume();
        canWatch = true;
        ArrayList<String> arr = getLocalizationFromDatabase();
        this.spinnerWeatherLocalizationAdapter.clear();
        this.spinnerWeatherLocalizationAdapter.addAll(arr);
        this.spinnerWeatherLocalizationAdapter.notifyDataSetChanged();
//        updateCitySpinner();
    }
    //TODO: perhaps to this in background?
    private void updateCitySpinner() {
        ArrayList<String> arr = getLocalizationFromDatabase();
        this.spinnerWeatherLocalizationAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arr);

        spinnerWeatherLocalizationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeatherLocalization.setAdapter(spinnerWeatherLocalizationAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();

        canWatch = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Settings settings);
        void citiesListClicked();
    }
}
