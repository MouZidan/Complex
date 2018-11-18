package com.mou.complex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class ComplexEvaluator {

    public static final String simpleExpressionRegex = "(?:[+-]?\\d+(?:\\.\\d+)?i?)+";
    private static final String TAG = "ComplexEvaluator";
    public static Pattern multiplicationPattern = Pattern.compile("\\d+(?:\\.\\d+)?\\*\\d+(?:\\.\\d+)?");
    public static Pattern divisionPattern = Pattern.compile("\\d+(?:\\.\\d+)?/\\d+(?:\\.\\d+)?");
    public static Pattern simpleTermPattern = Pattern.compile("[+-]?\\d+(?:\\.\\d+)?i?");
    public static Pattern polarExpressionPattern = Pattern.compile("-?\\d+(?:\\.\\d+)?\\\\angle-?\\d+(?:\\.\\d+)?");

    public static Complex evaluate(String expression) {
        //1. multiplication >>
        //2. division >>
        //3. replace polar with cartesian >>
        //3. addition/subtraction >>

        //replace multiplication operations
        Matcher matcher = multiplicationPattern.matcher(expression);
        String match;
        while (matcher.find()) {
            match = matcher.group();
            expression = expression.replace(match, multiply(match));
        }

        //replace division operations
        matcher = divisionPattern.matcher(expression);
        while (matcher.find()) {
            match = matcher.group();
            expression = expression.replace(match, divide(match));
        }
        //replace polar form with cartesian
        matcher = polarExpressionPattern.matcher(expression);
        while (matcher.find()) {
            match = matcher.group();
            expression = expression.replace(match, polarToCartesian(match));
        }

        if (!expression.matches(simpleExpressionRegex)) return null;
        // addition/subtraction
        matcher = simpleTermPattern.matcher(expression);
        float real = 0, imaginary = 0;
        while (matcher.find()) {
            match = matcher.group();
            if (match.contains("i")) {
                imaginary += Float.parseFloat(match.replace("i", ""));
            } else {
                real += Float.parseFloat(match);
            }
        }

        return new Complex(real, imaginary, Complex.CARTESIAN);
    }

    private static String divide(String expression) {
        String[] parts = expression.split("/");
        return "" + (Float.parseFloat(parts[0]) / Float.parseFloat(parts[1]));
    }

    private static String multiply(String expression) {
        String[] parts = expression.split("\\*");
        return "" + (Float.parseFloat(parts[0]) * Float.parseFloat(parts[1]));
    }

    private static String polarToCartesian(String polarForm) {
        String[] parts = polarForm.split("\\\\angle");
        Complex c = new Complex(Float.parseFloat(parts[0]), (float) Math.toRadians(Float.parseFloat(parts[1])), Complex.POLAR);
        if (c.getReal() >= 0)
            return "+" + c.toString();
        else return c.toString();
    }
}
