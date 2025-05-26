import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.implementation
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.type.ArtifactTypeDefinition

const val PATCHED_MIXIN = "patched-mixin"

fun Configuration.applyMixinPatches() =
    attributes {
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, PATCHED_MIXIN)
    }