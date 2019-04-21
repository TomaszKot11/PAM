package com.example.tomek.astroweatherone.mainActivity.fragments;

import android.content.Context;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.utilities.Settings;
import com.example.tomek.astroweatherone.utilities.ProjectConstants;
import com.example.tomek.astroweatherone.utilities.SharedPreferencesUtility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Spinner spinnerUnits;
    private ArrayAdapter<CharSequence> spinnerUnitsArrayAdapter;
    private Spinner spinnerValue;
    private ArrayAdapter<CharSequence> spinnerValueArrayAdapter;
    // to avoid triggering event watching on edit text when
    // text is set programmatically
    private boolean canWatch = false;
    private TextInputEditText latitudeEditText;
    private TextInputEditText longitutdeEditText;
    private String previousLatitudeValue;
    private String previousLongitudeValue;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
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

        latitudeEditText = getView().findViewById(R.id.latitudeInputEditText);
        longitutdeEditText = getView().findViewById(R.id.longitudeInputEditText);

        this.spinnerValueArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_numerical_values, android.R.layout.simple_spinner_item);
        this.spinnerUnitsArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_units, android.R.layout.simple_spinner_item);

        assingCharAdaptersToSpinners(spinnerValue, spinnerValueArrayAdapter);
        assingCharAdaptersToSpinners(spinnerUnits, spinnerUnitsArrayAdapter);

        this.previousLatitudeValue = String.valueOf(SharedPreferencesUtility.getLatitude(getActivity()));
        this.previousLongitudeValue = String.valueOf(SharedPreferencesUtility.getLongitude(getActivity()));


        configureActionListenersForControls();

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
            settings = new Settings(Double.parseDouble(longitutdeEditText.getText().toString()),
                    Double.parseDouble(latitudeEditText.getText().toString()),
                    Integer.parseInt(spinnerValue.getSelectedItem().toString()),
                    spinnerUnits.getSelectedItem().toString());

            return settings;
        } catch(NumberFormatException e) {
            Log.e("[SettingsFragment]", "NumberFormatException in SettingsFragment.");
            Log.e("[SettingsFragment]", e.toString());
        }

        return new Settings(Double.parseDouble(ProjectConstants.DMCS_LONGITUDE), Double.parseDouble(ProjectConstants.DMCS_LATITUDE), 5, "seconds");
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences settings = getActivity().getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);

        int position = spinnerUnitsArrayAdapter.getPosition(settings.getString(ProjectConstants.PREFERNCE_TIME_UNIT_KEY, "seconds"));
        spinnerUnits.setSelection(position);

        position = spinnerValueArrayAdapter.getPosition(settings.getString(ProjectConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        spinnerValue.setSelection(position);

        String value = settings.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE);
        latitudeEditText.setText(value);

        value = settings.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE);
        longitutdeEditText.setText(value);

        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
        Log.e("LONGITUDE:", String.valueOf(value));
    }

    @Override
    public void onResume() {
        super.onResume();

        canWatch = true;
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
        void onFragmentInteraction(Settings settings);
    }
}
