group = "top.bilitianx"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

plugins {
    idea
    kotlin("jvm") version "1.+"
    kotlin("plugin.serialization") version "1.+"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:+")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:+")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
    implementation("org.apache.poi:poi:+")
    implementation("org.apache.poi:poi-ooxml:+")
    implementation("org.apache.logging.log4j:log4j-api:+")
    implementation("org.apache.logging.log4j:log4j-core:+")
}