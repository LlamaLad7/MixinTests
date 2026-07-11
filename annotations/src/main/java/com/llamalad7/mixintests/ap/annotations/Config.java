package com.llamalad7.mixintests.ap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Config {
    String id() default "";

    int fabricCompat() default Integer.MAX_VALUE;

    Class<?> plugin() default void.class;
}
