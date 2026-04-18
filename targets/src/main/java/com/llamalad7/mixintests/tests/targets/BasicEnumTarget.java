package com.llamalad7.mixintests.tests.targets;

import com.llamalad7.mixintests.harness.tests.TestBox;

import java.util.Arrays;

public class BasicEnumTarget extends TestBox {
    @Override
    protected void box() {
        print("Values: " + Arrays.toString(BasicEnum.values()));
    }

    public enum BasicEnum {
        A, B, C;

        static {
            print("original <clinit>");
        }
    }
}
