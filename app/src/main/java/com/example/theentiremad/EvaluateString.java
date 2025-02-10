package com.example.theentiremad;

import java.util.*;

public class EvaluateString {

    public static double eval(String expression) {
        expression = expression.replaceAll("\\s+", "")
                .replace("x", "*")
                .replace("÷", "/");

        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("+", 1);
        precedence.put("-", 1);
        precedence.put("*", 2);
        precedence.put("/", 2);

        // Tokenization
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            } else {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }
                tokens.add(Character.toString(c));
            }
        }
        if (number.length() > 0) {
            tokens.add(number.toString());
        }

        // Shunting Yard algorithm
        for (String token : tokens) {
            if (token.matches("[0-9]+\\.?[0-9]*")) {
                outputQueue.add(token);
            } else if (precedence.containsKey(token)) {
                while (!operatorStack.isEmpty() &&
                        precedence.get(operatorStack.peek()) >= precedence.get(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                throw new IllegalArgumentException("Invalid character: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }

        // Evaluation
        Stack<Double> evaluationStack = new Stack<>();
        for (String token : outputQueue) {
            if (token.matches("[0-9]+\\.?[0-9]*")) {
                evaluationStack.push(Double.parseDouble(token));
            } else {
                double b = evaluationStack.pop();
                double a = evaluationStack.pop();
                switch (token) {
                    case "+":
                        evaluationStack.push(a + b);
                        break;
                    case "-":
                        evaluationStack.push(a - b);
                        break;
                    case "*":
                        evaluationStack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        evaluationStack.push(a / b);
                        break;
                }
            }
        }

        if (evaluationStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return evaluationStack.pop();
    }
}