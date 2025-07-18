package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class UnaryComparisonTarget extends TestBox {
    private int number = 10;

    @Override
    protected void box() {
        print(number > 0 ? "true" : "false");
    }
}
