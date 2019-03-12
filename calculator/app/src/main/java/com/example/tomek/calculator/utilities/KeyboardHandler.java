package com.example.tomek.calculator.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//TODO: Handle bug with backspace!
public class KeyboardHandler {


    private static KeyboardHandler instance;

    //TODO: on orientation change!
    public double prevValue = 0.0;
    public double currentValue = 0.0;
    public String operation = "";
    public boolean wasOperationClicked = false;
    public int cCLikcCounter = 0;

    public static KeyboardHandler getInstance() {
        if(instance == null) {
            synchronized (KeyboardHandler.class) {
                if(instance == null){
                    instance = new KeyboardHandler();
                }
            }
        }

        return instance;
    }



    private KeyboardHandler() { }


    public void handleOperation(TextView display, Button target, Context context) throws NumberFormatException {
        String btnText = target.getText().toString();

        if(btnText.matches("[0-9]")) {
            writeNumberToDisplay(btnText, display);

            if(!wasOperationClicked)
            {
                prevValue = Double.parseDouble(display.getText().toString());
            } else {
                currentValue = Double.parseDouble(display.getText().toString());
            }
        } else if(btnText.matches("[//*\\-+]")) {
            display.setText("0.0");
            wasOperationClicked = true;
            operation = btnText;

            //TODO: fix issue with the sign
            if(!wasOperationClicked)
            {
                prevValue = Double.parseDouble(display.getText().toString());
            } else {
                currentValue = Double.parseDouble(display.getText().toString());
            }

        } else if(btnText.equals("=")) {
            String result = performOperation(operation, prevValue, currentValue, context);

            display.setText(result);
        } else if(btnText.equals("C")) {
            cCLikcCounter++;


            if(cCLikcCounter % 2 == 0 ) {
                Toast.makeText(context, "Registers reseted!", Toast.LENGTH_SHORT).show();
                this.prevValue = 0.0;
                this.operation = "";
                this.wasOperationClicked = false;
            } else {
                display.setText("0.0");
                if(!wasOperationClicked) {
                    this.prevValue = 0.0;
                } else {
                    this.currentValue = 0.0;
                }
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
        } else if(btnText.equals("sin") || btnText.equals("cos") || btnText.equals("tan")) {
            String returnVal = handleTrygonometricFunctions(btnText, display.getText().toString());
            display.setText(returnVal);
        } else if(btnText.equals("ln")) {
          String displayString = display.getText().toString();
          if(displayString.equals("Wrong operation")) {
              Toast.makeText(context, "Wrong input should be floating point", Toast.LENGTH_SHORT).show();
          } else {
              double doubleValue = 0;
                try {
                    doubleValue = Double.parseDouble(displayString);
                } catch(NumberFormatException e) {
                    Toast.makeText(context, "Should be double value", Toast.LENGTH_SHORT).show();
                    return ;
                }
                double helper = this.prevValue;
                this.prevValue = Math.log(doubleValue);
                if(Double.isNaN(this.prevValue) || Double.isInfinite(this.prevValue)) {
                    Toast.makeText(context, "Value should be grater than 0", Toast.LENGTH_SHORT).show();
                    this.prevValue = helper;
                }

                display.setText(String.valueOf(this.prevValue));
          }

        } else if(btnText.equals("sqrt")) {

            double doubleValue = 0;
            try {
                doubleValue = Double.parseDouble(display.getText().toString());
            } catch(NumberFormatException e) {
                Toast.makeText(context, "This is not a number!", Toast.LENGTH_SHORT).show();
                return ;
            }

            if(doubleValue < 0) {
                Toast.makeText(context, "Value should be greater or equal to zero!", Toast.LENGTH_SHORT).show();
            } else {
                this.prevValue = Math.sqrt(doubleValue);

                display.setText(String.valueOf(this.prevValue));
            }
        } else {
            Toast.makeText(context, "No such operation present!", Toast.LENGTH_LONG).show();
        }

    }

//    private boolean isStringDouble(String stringToValidate) {
//        return stringToValidate.matches("^[0-9]+\\.?[0-9]+$");
//    }


    private String handleTrygonometricFunctions(String function, String value) {
        if(value.equals("Wrong operation")) return value;

        switch(function) {
            case "cos":
                this.prevValue = Math.cos(Double.parseDouble(value));
                return String.valueOf(this.prevValue);

            case "sin":
                this.prevValue = Math.sin(Double.parseDouble(value));
                return String.valueOf(this.prevValue);
            case "tan":
                this.prevValue = Math.sin(Double.parseDouble(value));
                return String.valueOf(this.prevValue);
            default:
                return "Wrong operation";
        }
    }


    private String performOperation(String operation, double prevValue, double currentValue, Context context) {
        double result = 0.0;
        switch(operation) {
            case "+":
                result = prevValue + currentValue;
                this.prevValue = result;
                return String.valueOf(result);
            case "-":
                result = prevValue - currentValue;
                this.prevValue = result;
                return String.valueOf(result);
            case "*":
                result = prevValue * currentValue;
                this.prevValue = result;
                return String.valueOf(result);
            case "/":
                if(currentValue == 0) {
                    Toast.makeText(context, "You can't divide by zero!",
                            Toast.LENGTH_LONG).show();
                    return "0.0";
                }
                result = prevValue / currentValue;
                this.prevValue = result;
                return String.valueOf(result);
            default:
                return "Wrong operation";
        }
    }

    private void writeNumberToDisplay(String number, TextView display) {
        String displayString = display.getText().toString();

        if(displayString.equals("0.0")) {
            display.setText(number);
            return;
        }

        if(displayString.contains(".")) {
            String[] splitString = displayString.split("\\.");
            if(splitString[1].charAt(0) == '0') {
                displayString = splitString[0];
                displayString += ".";
            }

        }

        displayString += number;
        display.setText(displayString);
    }
}
