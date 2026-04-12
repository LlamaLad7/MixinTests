package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class ParameterTarget extends TestBox {
    @Override
    protected void box() {
        doThing(27, "example");
    }

    private void doThing(int number, String string) {
        print("Number: " + number);
        print("String: " + string);
    }
}
