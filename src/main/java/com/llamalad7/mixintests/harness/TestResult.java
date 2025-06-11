package com.llamalad7.mixintests.harness;

import java.util.Map;

public record TestResult(String output, Map<String, byte[]> transformedClasses) {
}
