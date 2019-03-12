package com.example.tomek.calculator;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleCalculatorActivity extends Activity implements Calculable {


    TextView display;

    //TODO: on orientation change!
    double prevValue = 0.0;
    double currentValue = 0.0;
    String operation = "";
    boolean wasOperationClicked = false;
    int cCLikcCounter = 0;

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
            String btnText = target.getText().toString();

            if(btnText.matches("[0-9]")) {
                writeNumberToDisplay(btnText);

                if(!wasOperationClicked)
                {
                    Log.e("PREV VALUE", "PREV VALUE");
                    prevValue = Double.parseDouble(display.getText().toString());
                } else {
                    Log.e("Current VALUE", "Current VALUE");
                    currentValue = Double.parseDouble(display.getText().toString());
                }
            } else if(btnText.matches("[//*\\-+]")) {
                display.setText("0.0");
                wasOperationClicked = true;
                operation = btnText;

            } else if(btnText.equals("=")) {
//                wasOperationClicked = false;
                Log.e("Result", prevValue + " "+currentValue + " "+operation);
                String result = performOperation(operation, prevValue, currentValue);

                display.setText(result);
            } else if(btnText.equals("C")) {
                cCLikcCounter++;
                display.setText("0.0");

                if(cCLikcCounter % 2 == 0 ) {
                    this.prevValue = 0.0;
                    this.operation = "";
                    this.wasOperationClicked = false;
                }

            } else if(btnText.equals("+/-")) {
                String displayString = display.getText().toString();

                if(!(displayString.equals("0.0") || displayString.equals("0") || displayString.equals("Wrong operation"))) {
                    double displayValue = Double.parseDouble(displayString);
                    display.setText(String.valueOf(displayValue * (-1)));
                }
            } else if(btnText.equals(".")) {
                String displayString = display.getText().toString();

                if(!displayString.contains(".")) {
                    displayString += ".0";
                    display.setText(displayString);
                }
            } else if(btnText.equals("Bksp")) {
                String displayString = display.getText().toString();

                if(displayString.length() == 1) {
                    display.setText("0.0");
                    return;
                }

                if(!(displayString.equals("0.0") || displayString.equals("Wrong operation"))) {
                        if (displayString.charAt(displayString.length() - 2) == '.') {
                            displayString = displayString.substring(0, displayString.length() - 2);
                        } else {
                            displayString = displayString.substring(0, displayString.length() - 1);
                        }
                }

                display.setText(displayString);

                if(displayString.equals("")) display.setText("0.0");
            }
        }
    }

    private String performOperation(String operation, double prevValue, double currentValue) {
    double result = 0.0;
        switch(operation) {
            case "+":
                Log.e("Plus", "Plus");
                result = prevValue + currentValue;
                prevValue = result;
                return String.valueOf(result);
            case "-":
                Log.e("Minus", "Minus");
                result = prevValue - currentValue;
                prevValue = result;
                return String.valueOf(result);
            case "*":
                Log.e("multiply", "Multiply");
                result = prevValue * currentValue;
                prevValue = result;
                return String.valueOf(result);
            case "/":
                Log.e("div", "div");
                if(currentValue == 0) {
                    Toast.makeText(this, "You can't divide by zero!",
                            Toast.LENGTH_LONG).show();
                    return "0.0";
                }
                result = prevValue / currentValue;
                prevValue = result;
                return String.valueOf(result);
            default:
                return "Wrong operation";
        }
    }

    private void writeNumberToDisplay(String number) {
        String displayString = display.getText().toString();

        if(displayString.equals("0.0")) {
            display.setText(number);
            return;
        }

        if(displayString.contains(".")) {
            String[] splitString = displayString.split("\\.");
            for(String s : splitString) {
                Log.e("Co tam?", s);
            }
            if(splitString[1].charAt(0) == '0') {
                displayString = splitString[0];
                displayString += ".";
            }

        }

        displayString += number;
        display.setText(displayString);
    }
}
