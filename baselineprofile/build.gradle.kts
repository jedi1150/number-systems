plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "ru.sandello.binaryconverter.baselineprofile"
    compileSdk = 37

    defaultConfig {
        minSdk = 28
        targetSdk = 37

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            localDevices {
                create("ciPixel6Api34") {
                    device = "Pixel 6"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
            }
        }
    }

    flavorDimensions += listOf("flavor-type")
    productFlavors {
        create("prod") {
            dimension = "flavor-type"
            buildConfigField("String", "APP_PACKAGE_NAME", "\"ru.sandello.binaryconverter\"")
        }
        create("beta") {
            dimension = "flavor-type"
            buildConfigField("String", "APP_PACKAGE_NAME", "\"ru.sandello.binaryconverter.beta\"")
        }
        create("dev") {
            dimension = "flavor-type"
            buildConfigField("String", "APP_PACKAGE_NAME", "\"ru.sandello.binaryconverter.dev\"")
        }
    }
}

baselineProfile {
    val useManagedDevice = providers.gradleProperty("baselineprofile.managedDevice")
        .map { it.equals("true", ignoreCase = true) }
        .orElse(false)
    if (useManagedDevice.get()) {
        managedDevices += "ciPixel6Api34"
        useConnectedDevices = false
    } else {
        useConnectedDevices = true
    }
}

androidComponents {
    onVariants { variant ->
        val runnerArguments = variant.instrumentationRunnerArguments
        when (variant.buildType) {
            "nonMinifiedRelease" -> {
                runnerArguments.put(
                    "notClass",
                    listOf(
                        "ru.sandello.binaryconverter.baselineprofile.StartupBenchmark",
                        "ru.sandello.binaryconverter.baselineprofile.ScrollBenchmark",
                    ).joinToString(","),
                )
            }

            "benchmark" -> {
                runnerArguments.put(
                    "notClass",
                    "ru.sandello.binaryconverter.baselineprofile.BaselineProfileGenerator",
                )
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
