package com.example.tomek.astroweatherone;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.tomek.astroweatherone.utilities.StringConstants;

public class SettingsActivity extends AppCompatActivity {


    private Spinner spinnerUnits;
    private ArrayAdapter<CharSequence> spinnerUnitsArrayAdapter;
    private Spinner spinnerValue;
    private ArrayAdapter<CharSequence> spinnerValueArrayAdapter;

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

        this.spinnerValueArrayAdapter = ArrayAdapter.createFromResource(this, R.array.refreshing_numerical_values, android.R.layout.simple_spinner_item);
        this.spinnerUnitsArrayAdapter = ArrayAdapter.createFromResource(this, R.array.refreshing_units, android.R.layout.simple_spinner_item);

        assingCharAdaptersToSpinners(spinnerValue, spinnerValueArrayAdapter);
        assingCharAdaptersToSpinners(spinnerUnits, spinnerUnitsArrayAdapter);
    }

    private void assingCharAdaptersToSpinners(Spinner spinner, ArrayAdapter<CharSequence> arrayAdapter) {
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences settings = getSharedPreferences(StringConstants.SHARED_PREFERENCES_NAME, 0);

        int position = spinnerUnitsArrayAdapter.getPosition(settings.getString(StringConstants.PREFERNCE_TIME_UNIT_KEY, "seconds"));
        spinnerUnits.setSelection(position);

        position = spinnerValueArrayAdapter.getPosition(settings.getString(StringConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        spinnerValue.setSelection(position);

        String value = settings.getString(StringConstants.PREFERENCE_LATITTUDE_KEY, StringConstants.DMCS_LATITUDE);
        latitudeInputEditText.setText(value);

        value = settings.getString(StringConstants.PREFERENCES_LONGITUTDE_KEY, StringConstants.DMCS_LONGITUDE);
        longitutdeEditText.setText(value);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(StringConstants.SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(StringConstants.PREFENCES_TIME_VALUE_KEY, spinnerValue.getSelectedItem().toString());
        editor.putString(StringConstants.PREFERENCE_LATITTUDE_KEY, latitudeInputEditText.getText().toString());
        editor.putString(StringConstants.PREFERENCES_LONGITUTDE_KEY, longitutdeEditText.getText().toString());
        editor.putString(StringConstants.PREFERNCE_TIME_UNIT_KEY, spinnerUnits.getSelectedItem().toString());

        editor.commit();

    }





}
