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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:+")
//  Selenium
    implementation("org.seleniumhq.selenium:selenium-java:+")
//  HttpClient
    implementation("org.apache.httpcomponents.client5:httpclient5-fluent:5.+")
    implementation("com.squareup.okhttp3:okhttp:4.+")
//  JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:+")
//  POI
    implementation("org.apache.poi:poi:+")
    implementation("org.apache.poi:poi-ooxml:+")
//  Logging
    implementation("org.apache.logging.log4j:log4j-api:+")
    implementation("org.apache.logging.log4j:log4j-core:+")
}