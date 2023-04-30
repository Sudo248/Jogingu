//// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
//    var kotlin_version: String by extra
//    kotlin_version = "1.5.31"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ( Dependencies.gradle )
        classpath ( Dependencies.HILT_GRADLE )
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath(Dependencies.GOOGLE_SERVICE)
        classpath("com.google.gms:google-services:4.3.15")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
//        classpath(kotlinModule("gradle-plugin", Versions.kotlinVersion))
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}