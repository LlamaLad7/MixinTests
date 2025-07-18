package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class IntComparisonTarget implements TestBox {
    private final StringBuilder builder = new StringBuilder();

    @Override
    public String box() {
        int a = 65; // 'A'
        builder.append("a: ").append(a).append('\n');
        int b = 66; // 'B'
        builder.append("b == 'B':\n").append(b == 'B');
        return builder.toString();
    }
}
