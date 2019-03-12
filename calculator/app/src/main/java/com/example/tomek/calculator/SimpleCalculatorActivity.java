package com.example.tomek.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tomek.calculator.utilities.KeyboardHandler;

public class SimpleCalculatorActivity extends Activity implements Calculable {

    TextView display;
    KeyboardHandler keyboardHandler = KeyboardHandler.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        this.display = (TextView)findViewById(R.id.calculator_display);

        attachKeyboard();
    }


    private void attachKeyboard() {
        FrameLayout dynamicContent = findViewById(R.id.keyboard);
        View wizardView = getLayoutInflater()
                .inflate(R.layout.simple_keyboard, dynamicContent, false);
        dynamicContent.addView(wizardView);
    }


    public void keyboardHandler(View view) {
        if(view instanceof Button) {
            Button target = (Button)view;

            keyboardHandler.handleOperation(display, target, this);
        }
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(OPERATION_VALUE, this.keyboardHandler.operation);
        outState.putDouble(CURRENT_VALUE, this.keyboardHandler.currentValue);
        outState.putDouble(PREVIOUS_VALUE, this.keyboardHandler.prevValue);
        outState.putInt(CLICK_COUNTER_VALUE, this.keyboardHandler.cCLikcCounter);
        outState.putBoolean(WAS_OPERATION_CLICKED_VALUE, this.keyboardHandler.wasOperationClicked);
        outState.putString(DISPLAY_VALUE, display.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.keyboardHandler.operation = savedInstanceState.getString(OPERATION_VALUE);
        this.keyboardHandler.currentValue = savedInstanceState.getDouble(CURRENT_VALUE);
        this.keyboardHandler.prevValue = savedInstanceState.getDouble(PREVIOUS_VALUE);
        this.keyboardHandler.cCLikcCounter = savedInstanceState.getInt(CLICK_COUNTER_VALUE);
        this.keyboardHandler.wasOperationClicked = savedInstanceState.getBoolean(WAS_OPERATION_CLICKED_VALUE);
        this.display.setText(savedInstanceState.getString(DISPLAY_VALUE));
    }


}
