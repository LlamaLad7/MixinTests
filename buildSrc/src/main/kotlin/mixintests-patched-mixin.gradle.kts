plugins {
    java
}

dependencies {
    registerTransform(MixinPatchArtifactTransform::class) {
        from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar")
        to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, PATCHED_MIXIN)
    }
}

val mixinJar by configurations.registering {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false

    attributes {
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, PATCHED_MIXIN)
    }
}