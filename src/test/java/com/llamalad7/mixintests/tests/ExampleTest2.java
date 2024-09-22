package com.llamalad7.mixintests.tests;

import com.llamalad7.mixintests.ap.annotations.RunWithMixins;
import com.llamalad7.mixintests.mixins.ExampleMixin;
import com.llamalad7.mixintests.mixins.ExampleMixin2;
import com.llamalad7.mixintests.tests.targets.ExampleTarget;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@RunWithMixins({ExampleMixin2.class})
public class ExampleTest2 {
    @ParameterizedTest
    @MethodSource("range")
    public void test(int i) {
        assertEquals(12, ExampleTarget.getEleven());
    }

    private static Stream<Integer> range() {
        return IntStream.range(1, 1000).boxed();
    }
}
