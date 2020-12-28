@file:Suppress("PropertyName")

import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val GITHUB_URL = "https://github.com/bluefireoly/BlueUtils"

val JVM_VERSION = JavaVersion.VERSION_11
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "net.axay"
version = "1.0.4"

description = "A collection of utils I need for myself."

/*
 * PLUGINS
 */

plugins {

    `java-library`

    kotlin("jvm") version "1.4.21"

    `maven-publish`

    id("com.jfrog.bintray") version "1.8.5"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
}

dependencies {

    // MARIADB
    implementation("org.mariadb.jdbc", "mariadb-java-client", "2.7.0")

    // GSON
    implementation("com.google.code.gson", "gson", "2.8.6")

    // KMONGO and MONGODB
    implementation("org.litote.kmongo", "kmongo-core", "4.2.3")
    implementation("org.litote.kmongo", "kmongo-coroutine-core", "4.2.3")

}

/*
 * BUILD
 */

// JAVA VERSION

java {
    sourceCompatibility = JVM_VERSION
    targetCompatibility = JVM_VERSION

    withSourcesJar()
    withJavadocJar()
}

tasks {
    compileKotlin.configureJvmVersion()
    compileTestKotlin.configureJvmVersion()
}

/*
 * PUBLISHING
 */

bintray {

    user = project.findProperty("bintray.username") as? String ?: ""
    key = project.findProperty("bintray.api_key") as? String ?: ""

    setPublications(project.name)

    pkg.apply {

        version.apply {
            name = project.version.toString()
            released = Date().toString()
        }

        repo = project.name
        name = project.name

        setLicenses("Apache-2.0")

        vcsUrl = GITHUB_URL

    }

}

publishing {
    publications {
        create<MavenPublication>(project.name) {


            from(components["java"])

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = project.version.toString()

            pom {

                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("bluefireoly")
                    }
                }

                url.set(GITHUB_URL)
                scm { url.set(GITHUB_URL) }

            }

        }
    }
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString get() = majorVersion.let {
    val version = it.toInt()
    if (version <= 10) "1.$it" else it
}

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }
