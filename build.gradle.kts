plugins {
    id("java")
    `mixintests-patched-mixin`
    id("com.github.gmazzo.buildconfig") version "5.6.5"
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven")
    maven("https://maven.fabricmc.net/")
}

dependencies {
    implementation(project("targets"))
    testImplementation(testAnnotationProcessor(project("annotationprocessor"))!!)
    implementation(annotationProcessor("com.google.auto.service:auto-service:1.1.1")!!)
    compileOnly(testCompileOnly("org.spongepowered:mixin:0.8.7")!!)
    compileOnly(testCompileOnly("io.github.llamalad7:mixinextras-common:0.4.1")!!)
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("commons-io:commons-io:2.17.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.jimfs:jimfs:1.3.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.0")
    implementation("org.apache.logging.log4j:log4j-api:2.24.0")
    testCompileOnly("net.fabricmc:fabric-loader:0.16.5")
    implementation(platform("org.junit:junit-bom:5.11.0"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.platform:junit-platform-launcher:1.11.0")
    implementation("com.github.zafarkhaja:java-semver:0.10.2")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("com.roscopeco.jasm:jasm:0.7.0")
    implementation("org.opentest4j:opentest4j:1.3.0")
}

buildConfig {
    packageName("com.llamalad7.mixintests.harness")
    className("MixinArtifacts")
    useJavaOutput()

    buildConfigField("MIXIN_JARS", MIXIN_VERSIONS.associateWith(project::mixinJar))
    buildConfigField("FABRIC_MIXIN_JARS", FABRIC_MIXIN_VERSIONS.associateWith(project::fabricMixinJar))
    buildConfigField("MIXINEXTRAS_JARS", MIXINEXTRAS_VERSIONS.associateWith(project::mixinExtrasJar))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    test {
        java {
            // Needed for IntelliJ to run the tests with Gradle, for some reason.
            srcDirs(layout.buildDirectory.dir("generated/sources/annotationProcessor/java/test"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("mixin.debug.verbose", "true")
    systemProperty("mixin.debug.export", "true")
    if (project.hasProperty("force")) {
        systemProperty("forceGoldenTests", "true")
    }
}