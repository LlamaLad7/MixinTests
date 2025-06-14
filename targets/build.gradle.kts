plugins {
    id("java")
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":annotationprocessor"))
}