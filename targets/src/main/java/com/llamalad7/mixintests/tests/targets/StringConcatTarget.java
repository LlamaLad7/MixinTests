package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class StringConcatTarget implements TestBox {
    private final StringBuilder builder = new StringBuilder();
    private int number = 10;
    private double doubleNumber = -0.5;

    @Override
    public String box() {
        builder.append(number + " is a number\n");
        builder.append("this " + doubleNumber + " is negative\n");
        builder.append(new StringBuilder().append(number).append(" is a number\n").toString());
        builder.append(new StringBuilder().append("this ").append(doubleNumber).append(" is negative\n").toString());
        return builder.toString();
    }
}
