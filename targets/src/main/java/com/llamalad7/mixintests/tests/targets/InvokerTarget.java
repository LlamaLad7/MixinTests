package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

public class InvokerTarget extends TestBox {
    @Override
    protected void box() {
    }

    public String test() {
        return "test";
    }

    public String TEST() {
        return "TEST";
    }

    public String x() {
        return "x";
    }
}
