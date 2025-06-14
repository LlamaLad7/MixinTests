package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ExampleTarget implements TestBox {
    @Override
    public String box() {
        return "hello".repeat(2);
    }
}