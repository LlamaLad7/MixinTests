package com.llamalad7.mixintests.harness.util;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.harness.MixinArtifacts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public record MixinVersions(Version mixinVersion, @Nullable Version mixinExtrasVersion, boolean isFabric) {
    @NotNull
    @Override
    public String toString() {
        return toString(" ", ", ", UnaryOperator.identity());
    }

    public List<File> getJars() {
        List<File> jars = new ArrayList<>();
        jars.add(
                Objects.requireNonNullElse(
                        MixinArtifacts.MIXIN_JARS.get(mixinVersion.toString()),
                        MixinArtifacts.FABRIC_MIXIN_JARS.get(mixinVersion.toString())
                )
        );
        if (hasMixinExtras()) {
            jars.add(MixinArtifacts.MIXINEXTRAS_JARS.get(mixinExtrasVersion.toString()));
        }
        return jars;
    }

    public Version upstreamMixinVersion() {
        if (mixinVersion.buildMetadata().isEmpty()) {
            return mixinVersion;
        }
        return Version.parse(mixinVersion.toString().split("\\+mixin\\.")[1]);
    }

    public boolean isLatestMixin() {
        return mixinVersion.equals(MixinVersionInfo.MIXIN_VERSIONS.last()) ||
                mixinVersion.equals(MixinVersionInfo.FABRIC_MIXIN_VERSIONS.last());
    }

    public boolean isLatestMixinExtras() {
        return MixinVersionInfo.MIXINEXTRAS_VERSIONS.last().equals(mixinExtrasVersion);
    }

    public boolean hasMixinExtras() {
        return mixinExtrasVersion != null;
    }

    public String getSlug() {
        return toString("-", "-", String::toLowerCase);
    }

    private String toString(String separator, String groupSeparator, UnaryOperator<String> transform) {
        StringBuilder builder = new StringBuilder();
        if (isFabric) {
            builder.append(transform.apply("Fabric"));
            builder.append(separator);
        }
        builder.append(transform.apply("Mixin"));
        builder.append(separator);
        builder.append(isLatestMixin() ? "latest" : mixinVersion);
        if (hasMixinExtras()) {
            builder.append(groupSeparator);
            builder.append(transform.apply("MixinExtras"));
            builder.append(separator);
            builder.append(isLatestMixinExtras() ? "latest" : mixinExtrasVersion);
        }
        return builder.toString();
    }
}
