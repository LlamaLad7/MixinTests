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
    javaSourceSets.implementation("org.junit.jupiter:junit-jupiter:5.11.0")
}