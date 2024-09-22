package com.llamalad7.mixintests.ap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MixinConfigJson {
    public static final String PACKAGE = "com.llamalad7.mixintests.mixins";

    @SerializedName("package")
    private final String pkg = PACKAGE;

    private final String minVersion = "0.8";

    private final List<String> mixins;

    public MixinConfigJson(List<String> mixins) {
        this.mixins = mixins;
    }
}
