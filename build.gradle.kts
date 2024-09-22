plugins {
    id("java")
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
    implementation("org.spongepowered:mixin:0.8.7")
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("commons-io:commons-io:2.17.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.0")
    implementation("org.apache.logging.log4j:log4j-api:2.24.0")
    testCompileOnly("net.fabricmc:fabric-loader:0.16.5")
    implementation(platform("org.junit:junit-bom:5.11.0"))
    implementation("org.junit.jupiter:junit-jupiter")
    implementation("org.junit.platform:junit-platform-launcher:1.11.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
}