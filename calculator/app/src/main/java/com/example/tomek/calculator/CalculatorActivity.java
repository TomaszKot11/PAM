package com.example.tomek.calculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CalculatorActivity extends AppCompatActivity {

    private boolean isAdvanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        isAdvanced =getIntent().getExtras().getBoolean("advancedKeyboard");

        configureAppropriateKeyboardFragment();
    }


    private void configureAppropriateKeyboardFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = isAdvanced ? new AdvancedKeyboardFragment() : new BasicKeyboardFragment();
        fragmentTransaction.replace(R.id.keyboardFragment, fragment);
        fragmentTransaction.commit();
    }



}
