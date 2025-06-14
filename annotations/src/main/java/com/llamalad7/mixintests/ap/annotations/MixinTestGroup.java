package com.llamalad7.mixintests.ap.annotations;

import com.llamalad7.mixintests.harness.tests.TestBox;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MixinTestGroup {
    Class<? extends TestBox> box();

    String minMixin() default "";

    String minFabricMixin() default "";

    String minMixinExtras() default "";

    TestOption fabricMixin() default TestOption.BOTH;

    TestOption mixinExtras() default TestOption.OFF;
}
