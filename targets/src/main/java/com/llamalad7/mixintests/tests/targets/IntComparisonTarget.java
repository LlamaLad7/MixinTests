package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class IntComparisonTarget extends TestBox {
    @Override
    protected void box() {
        int a = 65; // 'A'
        print("a: " + a);
        int b = 66; // 'B'
        print("b == 'B': " + (b == 'B'));
    }
}
