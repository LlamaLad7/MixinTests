package com.llamalad7.mixintests.harness;

public class MixinConfig {
    public final String json;
    public final int fabricCompat;

    public MixinConfig(String json, int fabricCompat) {
        this.json = json;
        this.fabricCompat = fabricCompat;
    }
}
