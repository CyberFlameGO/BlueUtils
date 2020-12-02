@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_11
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "net.axay"
version = "1.0.0"

description = "A collection of utils I need for myself."

/*
 * PLUGINS
 */

plugins {

    `java-library`

    kotlin("jvm") version "1.4.10"

    maven

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

    // JETBRAINS ANNOTATIONS
    implementation("org.jetbrains", "annotations", "20.1.0")

    // KMONGO and MONGODB
    implementation("org.litote.kmongo", "kmongo-core", "4.1.3")

}

/*
 * BUILD
 */

// JAVA VERSION

java.sourceCompatibility = JVM_VERSION
java.targetCompatibility = JVM_VERSION

tasks {
    compileKotlin.configureJvmVersion()
    compileTestKotlin.configureJvmVersion()
}

// SOURCE CODE

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", sourcesJar)
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString get() = majorVersion.let {
    val version = it.toInt()
    if (version <= 10) "1.$it" else it
}

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }