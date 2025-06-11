import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.type.ArtifactTypeDefinition

const val PATCHED_MIXIN = "patched-mixin"

fun Configuration.applyMixinPatches() =
    attributes {
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, PATCHED_MIXIN)
    }