plugins {
    java
}

group = "com.llamalad7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javaSourceSets = JavaSourceSets()

dependencies {
    javaSourceSets.implementation(project(":annotations"))
}