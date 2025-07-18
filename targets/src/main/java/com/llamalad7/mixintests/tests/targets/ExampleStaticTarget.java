package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ExampleStaticTarget extends TestBox {
    @Override
    protected void box() {
        print(staticBox());
    }

    private static String staticBox() {
        return "hello".repeat(2);
    }
}
