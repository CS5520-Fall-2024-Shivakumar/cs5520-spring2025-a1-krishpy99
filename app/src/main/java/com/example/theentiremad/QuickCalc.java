package com.example.theentiremad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.theentiremad.EvaluateString;

public class QuickCalc extends AppCompatActivity implements View.OnClickListener {

    private TextView display;
    private StringBuilder currentInput = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_calc);

        // Initialize display view
        display = findViewById(R.id.textView2);

        // Initialize buttons and set click listeners
        int[] buttonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9, R.id.button_plus, R.id.button_minus,
                R.id.button_x, R.id.button_slash, R.id.button_equals
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }

        findViewById(R.id.button_clear).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentInput = new StringBuilder();
                        updateDisplay();
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "=":
                calculateResult();
                break;
            default:
                handleInput(buttonText);
                break;
        }
    }

    private void handleInput(String input) {
        // Prevent multiple operators in sequence
        if (isOperator(input) && endsWithOperator()) {
            currentInput.deleteCharAt(currentInput.length() - 1);
        }

        currentInput.append(input);
        updateDisplay();
    }

    private void calculateResult() {
        try {
            // Replace 'x' with '*' for proper evaluation
            String expression = currentInput.toString()
                    .replace('x', '*')
                    .replace('÷', '/');

            // Add your calculation logic here
            double result = EvaluateString.eval(expression);
            currentInput.setLength(0);
            currentInput.append(result);
            updateDisplay();
        } catch (Exception e) {
            currentInput.setLength(0);
            display.setText("Error");
        }
    }

    private void updateDisplay() {
        display.setText(currentInput.toString());
    }

    private boolean isOperator(String input) {
        return "+-x÷".contains(input);
    }

    private boolean endsWithOperator() {
        return currentInput.length() > 0 &&
                isOperator(String.valueOf(currentInput.charAt(currentInput.length() - 1)));
    }
}
