package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class InitialiserTarget extends TestBox {
    private static String STATIC_FIELD = "original";
    private String nonStaticField = "original";

    @Override
    protected void box() {
        print("Static: " + STATIC_FIELD);
        print("Non-static: " + nonStaticField);
    }
}
