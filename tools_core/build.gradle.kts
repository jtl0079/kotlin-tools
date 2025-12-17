// kotlin-tools:tools_core

plugins {
    `java-library`
    kotlin("jvm")
    //alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(20)
}

dependencies {
}


