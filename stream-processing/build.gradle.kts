import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/*
 * This file was generated by the Gradle "init" task.
 */

plugins {
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

tasks {

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("stream-processing")
        isZip64 = true
    }
}

dependencies {
    implementation("org.apache.spark:spark-hive_2.11:2.4.1")
    implementation("org.apache.spark:spark-streaming_2.11:2.4.1")
    implementation("org.apache.spark:spark-streaming-kafka-0-10_2.11:2.4.1")
    implementation("org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.1")
    implementation("org.postgresql:postgresql:9.4.1212")
    implementation("com.typesafe:config:1.3.4")
    implementation("com.fasterxml.jackson.module:jackson-module-scala_2.11:2.9.8")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.9.8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.8")
    implementation(project(":dto"))
}
