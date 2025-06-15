package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ExampleVoidTarget implements TestBox {
    @Override
    public String box() {
        StringBuilder builder = new StringBuilder("example");
        modify(builder);
        return builder.toString();
    }

    private void modify(StringBuilder builder) {
        builder.append(" output");
    }
}
