plugins {
    id("java")
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(annotationProcessor("com.google.auto.service:auto-service:1.1.1")!!)
    implementation("com.google.code.gson:gson:2.11.0")
}