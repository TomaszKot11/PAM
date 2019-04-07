package com.example.tomek.astroweatherone;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    // shared preferences constants
    public static final String SHARED_PREFERENCES_NAME = "APPLICATION_SETTINGS";
    public static final String PREFENCES_TIME_VALUE_KEY = "TIME_VALUE";
    public static final String PREFERNCE_TIME_UNIT_KEY = "TIME_UNIT";
    public static final String PREFERENCES_LONGITUTDE_KEY = "LONGITUDE_KEY";
    public static final String PREFERENCE_LATITTUDE_KEY = "LATITUDE_KEY";


    private Spinner spinnerUnits;
    private Spinner spinnerValue;
    private TextInputEditText latitudeInputEditText;
    private TextInputEditText longitutdeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerValue = findViewById(R.id.time_value_spinner);
        spinnerUnits = findViewById(R.id.time_unit_spinner);

        latitudeInputEditText = findViewById(R.id.latitudeInputEditText);
        longitutdeEditText = findViewById(R.id.longitudeInputEditText);

        assingCharAdaptersToSpinners(R.array.refreshing_numerical_values, spinnerValue);
        assingCharAdaptersToSpinners(R.array.refreshing_units, spinnerUnits);
    }

    private void assingCharAdaptersToSpinners(int  string_array_identifier, Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, string_array_identifier, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFENCES_TIME_VALUE_KEY, spinnerValue.getSelectedItem().toString());
        editor.putString(PREFERENCE_LATITTUDE_KEY, latitudeInputEditText.getText().toString());
        editor.putString(PREFERENCES_LONGITUTDE_KEY, longitutdeEditText.getText().toString());
        editor.putString(PREFERNCE_TIME_UNIT_KEY, spinnerUnits.getSelectedItem().toString());

        editor.commit();
    }





}
