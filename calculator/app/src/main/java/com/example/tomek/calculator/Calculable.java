package com.example.tomek.calculator;

import android.view.View;

public interface Calculable {
   static final String OPERATION_VALUE = "OperationValue";
   static final String CURRENT_VALUE = "CurrentValue";
   static final String PREVIOUS_VALUE = "PreviousValue";
   static final String CLICK_COUNTER_VALUE = "ClickCounterValue";
   static final String WAS_OPERATION_CLICKED_VALUE = "WasOperationClickedValue";
   static final String DISPLAY_VALUE = "DisplayValue";

    void keyboardHandler(View view);
}

