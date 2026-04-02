package com.llamalad7.mixintests.harness;

public class MixinConfig {
    public final String json;
    public final Integer fabricCompat;

    public MixinConfig(String json, Integer fabricCompat) {
        this.json = json;
        this.fabricCompat = fabricCompat;
    }
}
