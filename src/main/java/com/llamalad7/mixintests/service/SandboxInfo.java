package com.llamalad7.mixintests.service;

public record SandboxInfo(String mixinConfig) {
    public static SandboxInfo getInstance() {
        return TransformingClassLoader.getInstance().getSandboxInfo();
    }
}
