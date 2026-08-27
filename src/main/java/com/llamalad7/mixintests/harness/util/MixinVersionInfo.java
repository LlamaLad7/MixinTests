package com.llamalad7.mixintests.harness.util;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.harness.MixinArtifacts;

import java.util.*;
import java.util.stream.Collectors;

public class MixinVersionInfo {
    private static final Version DUMMY_MIN = Version.of(0, 0, 0);
    public static final SortedSet<Version> MIXIN_VERSIONS = collectVersions(MixinArtifacts.MIXIN_JARS);
    public static final SortedSet<Version> FABRIC_MIXIN_VERSIONS = collectVersions(MixinArtifacts.FABRIC_MIXIN_JARS);
    public static final SortedSet<Version> MIXINEXTRAS_VERSIONS = collectVersions(MixinArtifacts.MIXINEXTRAS_JARS);
    private static final SortedMap<Version, Version> MIXIN_REQUIRED_MIXINEXTRAS_BUMPS = collectVersionMap(MixinArtifacts.MIXIN_REQUIRED_MIXINEXTRAS_BUMPS);
    private static final SortedMap<Version, Version> FABRIC_MIXIN_REQUIRED_MIXINEXTRAS_BUMPS = collectVersionMap(MixinArtifacts.FABRIC_MIXIN_REQUIRED_MIXINEXTRAS_BUMPS);

    public static Version minMixinExtrasForMixin(Version mixinVersion, boolean isFabric) {
        SortedMap<Version, Version> bumps = isFabric ? FABRIC_MIXIN_REQUIRED_MIXINEXTRAS_BUMPS : MIXIN_REQUIRED_MIXINEXTRAS_BUMPS;
        return Optional.ofNullable(bumps.headMap(mixinVersion).lastEntry())
                .map(Map.Entry::getKey)
                .orElse(DUMMY_MIN);
    }

    private static SortedSet<Version> collectVersions(Map<String, ?> versions) {
        return versions.keySet().stream().map(Version::parse).collect(Collectors.toCollection(TreeSet::new));
    }

    private static SortedMap<Version, Version> collectVersionMap(Map<String, String> bumps) {
        return bumps.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> Version.parse(entry.getKey()),
                        entry -> Version.parse(entry.getValue()),
                        (a, b) -> a,
                        TreeMap::new
                )
        );
    }
}
