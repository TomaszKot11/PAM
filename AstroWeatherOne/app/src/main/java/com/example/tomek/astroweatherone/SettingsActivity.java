package com.example.tomek.astroweatherone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerUnits;
    private Spinner spinnerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerValue = findViewById(R.id.time_value_spinner);
        spinnerUnits = findViewById(R.id.time_unit_spinner);

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
}
