package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class BoxedComparisonTarget extends TestBox {
    @Override
    protected void box() {
        print(Integer.valueOf(10) > 0);
        print(Integer.valueOf(10) <= 5);
    }
}
