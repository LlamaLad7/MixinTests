package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class AccessorTarget extends TestBox {
    private final String test = "original";
    private final String TEST = "original";
    private final String x = "original";

    @Override
    protected void box() {
        print("test: " + test);
        print("TEST: " + TEST);
        print("x: " + x);
    }
}
