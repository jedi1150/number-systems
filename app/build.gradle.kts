import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.gms)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "ru.sandello.binaryconverter"
    compileSdk = 36
    defaultConfig {
        applicationId = "ru.sandello.binaryconverter"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "2.4.1"
        ndk {
            debugSymbolLevel = "FULL"
        }
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    flavorDimensions += listOf("flavor-type")
    productFlavors {
        create("prod") {
            dimension = "flavor-type"
        }
        create("beta") {
            dimension = "flavor-type"
            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta"
        }
        create("dev") {
            dimension = "flavor-type"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }
    bundle {
        language {
            enableSplit = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":lib"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.proto)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.android.material)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.android.play.review.ktx)

    // Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)
}
