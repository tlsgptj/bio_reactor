// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

}
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri ("https://raw.githubusercontent.com/alexgreench/google-webrtc/master") }
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:4.4.2")
        classpath ("com.google.gms:google-services:4.4.2") // Google Services plugin
    }
}
