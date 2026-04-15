package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class TargetWithLocalVariables extends TestBox {
    private static final StringBuilder builder = new StringBuilder();

    @Override
    protected void box() {
        doThing(builder);
        staticDoThing(builder);
        print(builder);
    }

    private void doThing(StringBuilder builder) {
        int a = 1;
        int b = 10;
        builder.append(a + b).append('\n');
    }

    private static void staticDoThing(StringBuilder builder) {
        int a = 1;
        int b = 10;
        builder.append(a * b).append('\n');
    }
}
