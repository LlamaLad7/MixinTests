package com.llamalad7.mixintests.ap.annotations;

public enum TestOption {
    ON, OFF, BOTH;

    public boolean permits(boolean value) {
        return value ? this != OFF : this != ON;
    }
}
