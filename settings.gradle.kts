plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "MixinTests"
include("annotations")
include("targets")

providers.gradleProperty("fabricMixinDir").orNull?.let { dir ->
    includeBuild(dir) {
        dependencySubstitution {
            substitute(module("net.fabricmc:sponge-mixin:1.0.0+mixin.1.0.0")).using(project(":"))
        }
    }
}