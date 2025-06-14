package com.llamalad7.mixintests.harness.util;

public class CiUtil {
    public static final boolean IS_CI = System.getenv("CI") != null;
}
