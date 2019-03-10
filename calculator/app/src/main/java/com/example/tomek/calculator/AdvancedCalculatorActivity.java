package com.example.tomek.calculator;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

//TODO: fix issue with keyboard not taking whole available height
public class AdvancedCalculatorActivity extends Activity implements Calculable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        attachKeyboard();
    }


    private void attachKeyboard() {
        FrameLayout dynamicContent = findViewById(R.id.keyboard);
        View wizardView = getLayoutInflater()
                .inflate(R.layout.advanced_keyboard, dynamicContent, false);
        dynamicContent.addView(wizardView);
    }

   public void keyboardHandler(View view) {
        Toast.makeText(this, "This is my Toast message!",
                Toast.LENGTH_LONG).show();
    }
}
