plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
//    id("com.google.gms.google-services") // Uncomment this line to use google-services
//    id("com.google.firebase.crashlytics")
}

android {
    namespace = "ru.sandello.binaryconverter"
    compileSdk = 33
    defaultConfig {
        applicationId = "ru.sandello.binaryconverter"
        minSdk = 21
        targetSdk = 33
        versionCode = 113
        versionName = "2.0.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
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

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}
dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.github.jedi1150.numsys)

//    implementation "androidx.browser:browser:1.4.0"

    // Firebase
//    implementation platform("com.google.firebase:firebase-bom:$firebase_bom_version")
//    implementation "com.google.firebase:firebase-crashlytics-ktx"
//    implementation "com.google.firebase:firebase-analytics-ktx"

    // Play Core
//    implementation "com.google.android.play:core:1.10.3"
//    implementation "com.google.android.play:core-ktx:1.8.1"

    // Ads
//    implementation "com.google.android.gms:play-services-ads:20.6.0"
}
