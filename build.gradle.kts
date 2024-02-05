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

kotlin {
    jvmToolchain(17)
}

group = "top.bilitianx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
// Crawl
    implementation("org.seleniumhq.selenium:selenium-java:+")
    implementation("org.jsoup:jsoup:+")
// JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:+")
// POI
    implementation("org.apache.poi:poi:+")
    implementation("org.apache.poi:poi-ooxml:+")
// Logging
    implementation("org.apache.logging.log4j:log4j-api:+")
    implementation("org.apache.logging.log4j:log4j-core:+")
}