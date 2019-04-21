package com.example.tomek.astroweatherone.mainActivity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.utilities.Settings;
import com.example.tomek.astroweatherone.utilities.StringConstants;

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

    private TextInputEditText latitudeInputEditText;
    private TextInputEditText longitutdeEditText;

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
    // TODO: Rename and change types and number of parameters
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

        latitudeInputEditText = getView().findViewById(R.id.latitudeInputEditText);
        longitutdeEditText = getView().findViewById(R.id.longitudeInputEditText);

        this.spinnerValueArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_numerical_values, android.R.layout.simple_spinner_item);
        this.spinnerUnitsArrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.refreshing_units, android.R.layout.simple_spinner_item);

        assingCharAdaptersToSpinners(spinnerValue, spinnerValueArrayAdapter);
        assingCharAdaptersToSpinners(spinnerUnits, spinnerUnitsArrayAdapter);


        configureActionListenersForControls();

   }

    private void assingCharAdaptersToSpinners(Spinner spinner, ArrayAdapter<CharSequence> arrayAdapter) {
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
    }

    private void configureActionListenersForControls() {
        latitudeInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(SettingsFragment.this.mListener != null) {
                    Settings settings = SettingsFragment.this.produceSettingsFromControls();
                    SettingsFragment.this.mListener.onFragmentInteraction(settings);
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
                if(SettingsFragment.this.mListener != null) {
                    Settings settings = SettingsFragment.this.produceSettingsFromControls();
                    SettingsFragment.this.mListener.onFragmentInteraction(settings);
                }
            }
        });

        longitutdeEditText.setOnEditorActionListener((adapterView, view, i) -> {
            if(SettingsFragment.this.mListener != null) {
                Settings settings = SettingsFragment.this.produceSettingsFromControls();
                SettingsFragment.this.mListener.onFragmentInteraction(settings);
            }
            return true;
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



    private Settings produceSettingsFromControls() {
        Settings settings;
        try {
            settings = new Settings(Double.parseDouble(longitutdeEditText.getText().toString()),
                    Double.parseDouble(latitudeInputEditText.getText().toString()),
                    Integer.parseInt(spinnerValue.getSelectedItem().toString()),
                    spinnerUnits.getSelectedItem().toString());

            return settings;
        } catch(NumberFormatException e) {
            Log.e("[SettingsFragment]", "NumberFormatException in SettingsFragment.");
        }

        return new Settings(Double.parseDouble(StringConstants.DMCS_LONGITUDE), Double.parseDouble(StringConstants.DMCS_LATITUDE), 5, "seconds");
    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences settings = getActivity().getSharedPreferences(StringConstants.SHARED_PREFERENCES_NAME, 0);

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
        void onFragmentInteraction(Settings settings);
    }
}
