package com.llamalad7.mixintests.tests;

import com.llamalad7.mixintests.ap.annotations.RunWithMixins;
import com.llamalad7.mixintests.mixins.ExampleMixin;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@RunWithMixins({ExampleMixin.class})
public class ExampleTest {
    @ParameterizedTest
    @MethodSource("range")
    public void test(int i) {
        assertEquals(11, ExampleTarget.getEleven());
    }

    private static Stream<Integer> range() {
        return IntStream.range(1, 10000).boxed();
    }
}
