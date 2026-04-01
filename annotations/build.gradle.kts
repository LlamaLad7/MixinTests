plugins {
    java
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(annotationProcessor("com.google.auto.service:auto-service:1.1.1")!!)
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("org.junit.jupiter:junit-jupiter:5.11.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}