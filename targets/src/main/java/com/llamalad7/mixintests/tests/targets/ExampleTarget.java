package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ExampleTarget extends TestBox {
    @Override
    protected void box() {
        print("hello".repeat(2));
    }
}