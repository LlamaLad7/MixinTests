import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import java.io.File

fun Project.mixinJar(version: String): File =
    getJar("org.spongepowered:mixin", version) {
        applyMixinPatches()
    }

fun Project.fabricMixinJar(version: String): File =
    getJar("net.fabricmc:sponge-mixin", version) {
        applyMixinPatches()
    }

fun Project.mixinExtrasJar(version: String): File =
    getJar("io.github.llamalad7:mixinextras-common", version)

private fun Project.getJar(artifact: String, version: String, routine: Configuration.() -> Unit = {}): File {
    val config = configurations.detachedConfiguration(
        dependencies.create("$artifact:$version")
    ).apply {
        isTransitive = false
    }
    config.routine()
    return config.resolve().single()!!
}