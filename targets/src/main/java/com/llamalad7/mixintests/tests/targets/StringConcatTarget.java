package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class StringConcatTarget extends TestBox {
    private int number = 10;
    private double doubleNumber = -0.5;

    @Override
    protected void box() {
        print(number + " is a number");
        print("this " + doubleNumber + " is negative");
        print(new StringBuilder().append(number).append(" is a number").toString());
        print(new StringBuilder().append("this ").append(doubleNumber).append(" is negative").toString());
    }
}
