import org.gradle.api.Project

const val LOCAL_FABRIC_MIXIN_VERSION = "1.0.0+mixin.1.0.0"
const val LOCAL_MIXINEXTRAS_VERSION = "1.0.0"

val MIXIN_VERSIONS = listOf(
    "0.8.4",
    "0.8.5",
    "0.8.7",
)

val Project.FABRIC_MIXIN_VERSIONS
    get() = listOf(
        "0.10.7+mixin.0.8.4",
        "0.13.4+mixin.0.8.5",
        "0.15.5+mixin.0.8.7",
        "0.17.4+mixin.0.8.7",
    ) + listOf(LOCAL_FABRIC_MIXIN_VERSION).filter { hasProperty("fabricMixinDir") }

val Project.MIXINEXTRAS_VERSIONS
    get() = listOf(
        "0.5.5",
    ) + listOf(LOCAL_MIXINEXTRAS_VERSION).filter { hasProperty("mixinExtrasDir") }

val MIXIN_REQUIRED_MIXINEXTRAS_BUMPS = emptyMap<String, String>()

val FABRIC_MIXIN_REQUIRED_MIXINEXTRAS_BUMPS = emptyMap<String, String>()
