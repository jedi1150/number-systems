plugins {
    alias(libs.plugins.binaryCompatibilityValidator)
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    explicitApi()
}

dependencies {
    testImplementation(kotlin("test"))
}
