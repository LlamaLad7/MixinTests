package com.llamalad7.mixintests.harness;

import java.util.Map;

public final class TestResult {
    public final String output;
    public final Throwable error;
    public final Map<String, byte[]> transformedClasses;

    public TestResult(String output, Throwable error, Map<String, byte[]> transformedClasses) {
        this.output = output;
        this.error = error;
        this.transformedClasses = transformedClasses;
    }
}
