import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.objectweb.asm.ClassVisitor

abstract class MixinPatchArtifactTransform : TransformAction<TransformParameters.None> {
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val outputFile = outputs.file(inputFile.name.replace(".jar", "-patched.jar"))
        transformJar(inputFile, outputFile) { node ->
            for (transformer in transformers) {
                transformer(node)
            }
        }
    }

    private companion object {
        val transformers = listOf(
            ::makeUuidsDeterministic,
            ::changeExportDir,
        )
    }
}
