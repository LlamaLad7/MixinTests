package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class UnaryComparisonTarget implements TestBox {
    private final StringBuilder builder = new StringBuilder();
    private int number = 10;

    @Override
    public String box() {
        builder.append(number > 0 ? "true" : "false");
        return builder.toString();
    }
}
