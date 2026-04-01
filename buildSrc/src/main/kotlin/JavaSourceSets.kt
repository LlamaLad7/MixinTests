import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.kotlin.dsl.the

class JavaSourceSets internal constructor(
    private val project: Project,
    private val isTest: Boolean,
    private val javaVersions: List<Int>
) {
    init {
        with(project) {
            the<SourceSetContainer>().apply {
                for (version in javaVersions) {
                    val name = sourceSetName(version)
                    maybeCreate(name).apply {
                        java.srcDirs.add(file("src/$name"))
                        if (isTest) {
                            val main = getByName("main")
                            compileClasspath += main.output
                            runtimeClasspath += main.output
                            compileClasspath += main.compileClasspath
                            runtimeClasspath += main.runtimeClasspath
                        }
                    }
                }
            }

            for (version in javaVersions.dropLast(1)) {
                tasks.named<JavaCompile>(compileTaskName(version)) {
                    javaCompiler.set(
                        serviceOf<JavaToolchainService>().compilerFor {
                            languageVersion.set(JavaLanguageVersion.of(version))
                        }
                    )
                }
            }

            the<JavaPluginExtension>().apply {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(javaVersions.last()))
                }
            }
        }

        if (isTest) {
            setupTestTasks()
        } else {
            setupConfigurations()
        }
    }

    fun <T : Any> implementation(dependency: T) = dependencyHelper("implementation", dependency)

    fun <T : Any> compileOnly(dependency: T) = dependencyHelper("compileOnly", dependency)

    fun <T : Any> annotationProcessor(dependency: T) = dependencyHelper("annotationProcessor", dependency)

    fun versionedDependencies(configuration: String, projectPath: String) {
        with(project) {
            for (ourVersion in javaVersions) {
                for (theirVersion in javaVersions.takeWhile { it <= ourVersion }) {
                    dependencies {
                        configurationName(configuration, ourVersion)(
                            project(
                                projectPath,
                                configuration = publishedConfigurationName(theirVersion)
                            )
                        )
                    }
                }
            }
        }
    }

    fun configureEach(action: SourceSet.() -> Unit) {
        for (version in javaVersions) {
            project.the<SourceSetContainer>().getByName(sourceSetName(version)).apply {
                action()
            }
        }
    }

    private fun setupTestTasks() {
        val sourceSets = project.the<SourceSetContainer>()
        for (version in javaVersions.dropLast(1)) {
            val task = project.tasks.register<Test>(testTaskName(version)) {
                testClassesDirs = sourceSets[sourceSetName(version)].output.classesDirs
                classpath = sourceSets[sourceSetName(version)].runtimeClasspath
            }
            project.tasks.named("check").configure {
                dependsOn(task)
            }
        }
    }

    private fun setupConfigurations() {
        for (version in javaVersions) {
            val configurationName = publishedConfigurationName(version)
            project.configurations.create(configurationName) {
                isCanBeConsumed = true
                isCanBeResolved = false
            }
            project.artifacts {
                add(
                    configurationName,
                    project.tasks.named<JavaCompile>(compileTaskName(version)).map { it.destinationDirectory }
                )
            }
        }
    }

    private fun <T : Any> dependencyHelper(configuration: String, dependency: T): T {
        project.dependencies {
            for (version in javaVersions) {
                configurationName(configuration, version)(dependency)
            }
        }
        return dependency
    }

    private fun sourceSetName(version: Int) =
        if (version == javaVersions.last()) {
            if (isTest) "test" else "main"
        } else {
            if (isTest) "testJava${version}" else "java${version}"
        }

    private fun compileTaskName(version: Int) =
        if (version == javaVersions.last()) {
            if (isTest) "compileTestJava" else "compileJava"
        } else {
            "compile${sourceSetName(version).capitalize()}Java"
        }

    private fun testTaskName(version: Int) =
        if (version == javaVersions.last()) {
            "test"
        } else {
            "test${sourceSetName(version).capitalize()}"
        }

    private fun configurationName(configuration: String, version: Int) =
        if (version == javaVersions.last()) {
            if (isTest) "test${configuration.capitalize()}" else configuration
        } else {
            "${sourceSetName(version)}${configuration.capitalize()}"
        }

    private fun publishedConfigurationName(version: Int) =
        "java${version}ApiElements"

    companion object {
        val DEFAULT_VERSIONS = listOf(8, 25)
    }
}

fun Project.JavaSourceSets(isTest: Boolean = false, javaVersions: List<Int> = JavaSourceSets.DEFAULT_VERSIONS) =
    JavaSourceSets(this, isTest, javaVersions)

private fun String.capitalize() = replaceFirstChar { it.uppercaseChar() }