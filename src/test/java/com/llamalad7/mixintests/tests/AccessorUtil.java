package com.llamalad7.mixintests.tests;

import java.lang.reflect.Method;

public final class AccessorUtil {
    public static Method[] getMethods(Class<?> clazz, String... names) {
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
        return String.format("%s: %04x", m, m.getModifiers());
    }

    public static Method findUniqueMethod(Class<?> clazz, String name) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith(name)) {
                return method;
            }
        }
        throw new RuntimeException("Unique method: " + name + " was not found");
    }

    private AccessorUtil() {
    }
}
