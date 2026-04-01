package com.llamalad7.mixintests.harness;

import java.util.Map;

public final class TestResult {
    public final String output;
    public final Map<String, byte[]> transformedClasses;

    public TestResult(String output, Map<String, byte[]> transformedClasses) {
        this.output = output;
        this.transformedClasses = transformedClasses;
    }
}
