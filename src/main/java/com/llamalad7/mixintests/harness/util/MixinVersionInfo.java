package com.llamalad7.mixintests.harness.util;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixintests.harness.MixinArtifacts;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MixinVersionInfo {
    public static final SortedSet<Version> MIXIN_VERSIONS = collectVersions(MixinArtifacts.MIXIN_JARS);
    public static final SortedSet<Version> FABRIC_MIXIN_VERSIONS = collectVersions(MixinArtifacts.FABRIC_MIXIN_JARS);
    public static final SortedSet<Version> MIXINEXTRAS_VERSIONS = collectVersions(MixinArtifacts.MIXINEXTRAS_JARS);

    private static SortedSet<Version> collectVersions(Map<String, ?> versions) {
        return versions.keySet().stream().map(Version::parse).collect(Collectors.toCollection(TreeSet::new));
    }
}
