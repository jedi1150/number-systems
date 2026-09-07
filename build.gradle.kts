import org.gradle.api.attributes.Bundling
import org.gradle.language.base.plugins.LifecycleBasePlugin

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.firebaseCrashlytics) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.screenshot) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.detekt) apply false
}

val ktlint = configurations.create("ktlint")

dependencies {
    ktlint(libs.ktlint.cli) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val ktlintInputArgs = listOf(
    "**/src/**/*.kt",
    "**/*.kts",
    "!**/build/**",
    "!**/generated/**",
)

tasks.register<JavaExec>("ktlintCheck") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style with ktlint 2.x"
    classpath = ktlint
    mainClass.set("io.github.ktlint.core.Main")
    args(ktlintInputArgs)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Format Kotlin code style with ktlint 2.x"
    classpath = ktlint
    mainClass.set("io.github.ktlint.core.Main")
    args(listOf("-F") + ktlintInputArgs)
}

subprojects {
    plugins.withId("dev.detekt") {
        configure<dev.detekt.gradle.extensions.DetektExtension> {
            buildUponDefaultConfig = true
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
            baseline.set(file("$rootDir/config/detekt/baseline-${project.name}.xml"))
        }
    }
}
