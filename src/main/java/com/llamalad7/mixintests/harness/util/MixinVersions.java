package com.llamalad7.mixintests.harness.util;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.harness.MixinArtifacts;

import java.io.File;
import java.util.List;
import java.util.Objects;

public record MixinVersions(Version mixinVersion, Version mixinExtrasVersion) {
    @Override
    public String toString() {
        return "Mixin %s, MixinExtras %s".formatted(mixinVersion, mixinExtrasVersion);
    }

    public List<File> getJars() {
        return List.of(
                Objects.requireNonNullElse(
                        MixinArtifacts.MIXIN_JARS.get(mixinVersion.toString()),
                        MixinArtifacts.FABRIC_MIXIN_JARS.get(mixinVersion.toString())
                ),
                MixinArtifacts.MIXINEXTRAS_JARS.get(mixinExtrasVersion.toString())
        );
    }

    public Version upstreamMixinVersion() {
        if (mixinVersion.buildMetadata().isEmpty()) {
            return mixinVersion;
        }
        return Version.parse(mixinVersion.toString().split("\\+mixin\\.")[1]);
    }
}
