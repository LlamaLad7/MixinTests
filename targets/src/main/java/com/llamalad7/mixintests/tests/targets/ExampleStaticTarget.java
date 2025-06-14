package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ExampleStaticTarget implements TestBox {
    @Override
    public String box() {
        return staticBox();
    }

    private static String staticBox() {
        return "hello".repeat(2);
    }
}
