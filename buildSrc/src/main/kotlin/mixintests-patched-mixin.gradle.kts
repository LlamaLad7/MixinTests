plugins {
    java
}

dependencies {
    registerTransform(MixinPatchArtifactTransform::class) {
        from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar")
        to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, PATCHED_MIXIN)
    }
}
