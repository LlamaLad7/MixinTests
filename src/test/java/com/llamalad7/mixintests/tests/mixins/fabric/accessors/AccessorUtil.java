package com.llamalad7.mixintests.tests.mixins.fabric.accessors;

import java.lang.reflect.Method;

final class AccessorUtil {
    public static Method[] getMethods(Class<?> clazz, String ...names) {
        Method[] methods = new Method[names.length];

        for (int i = 0; i < names.length; i++) {
            try {
                methods[i] = clazz.getDeclaredMethod(names[i]);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        return methods;
    }
    public static String formatMethod(Method m) {
        return m + ": " + String.format("%04x", m.getModifiers());
    }

    private AccessorUtil() {}
}
