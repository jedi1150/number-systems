import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
    alias(libs.plugins.kotlin.jvm)
}

@OptIn(ExperimentalAbiValidation::class)
kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
    abiValidation()
}

dependencies {
    testImplementation(kotlin("test"))
}
