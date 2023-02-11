buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
        classpath(libs.crashlytics.gradlePlugin)
        classpath(libs.google.services)
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
