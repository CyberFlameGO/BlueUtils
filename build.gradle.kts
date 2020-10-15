@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
*/

val JVM_VERSION = JavaVersion.VERSION_1_8
val JVM_VERSION_STRING = "1.8"

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

    java
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
    implementation("org.mariadb.jdbc", "mariadb-java-client", "2.6.2")

    // GSON
    implementation("com.google.code.gson", "gson", "2.8.6")

    // JETBRAINS ANNOTATIONS
    implementation("org.jetbrains", "annotations", "19.0.0")

    // KMONGO
    implementation("org.litote.kmongo", "kmongo", "4.0.3")

}

/*
 * BUILD
 */

// JAVA VERSION

java.sourceCompatibility = JVM_VERSION
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JVM_VERSION_STRING
    }
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