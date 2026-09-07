package ru.sandello.binaryconverter.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

internal const val TIMEOUT_MS = 8_000L
internal const val OPTIONAL_UI_TIMEOUT_MS = 2_000L
internal const val BENCHMARK_ITERATIONS = 5
internal const val PROFILE_MAX_ITERATIONS = 3
internal const val PROFILE_STABLE_ITERATIONS = 2

internal fun targetPackageName(): String = BuildConfig.APP_PACKAGE_NAME

internal fun tagSelector(tag: String): BySelector = By.res(tag)

internal fun MacrobenchmarkScope.startActivityAndWaitForContent() {
    startActivityAndWait()
    waitForTag(BenchmarkTags.CONVERTER_DEC)
}

internal fun MacrobenchmarkScope.exerciseCriticalUserJourney() {
    waitForTag(BenchmarkTags.CONVERTER_DEC)
    device.findObject(tagSelector(BenchmarkTags.CONVERTER_DEC))?.text = "255"

    device.findObject(tagSelector(BenchmarkTags.CONVERTER_CUSTOM_RADIX))?.click()
    if (device.wait(Until.hasObject(By.text("12")), OPTIONAL_UI_TIMEOUT_MS)) {
        device.findObject(By.text("12"))?.click()
    }

    device.findObject(tagSelector(BenchmarkTags.NAV_CALCULATOR))?.click()
    waitForTag(BenchmarkTags.CALCULATOR_OPERAND_1)
    device.findObject(tagSelector(BenchmarkTags.CALCULATOR_OPERAND_1))?.text = "10"

    device.findObject(tagSelector(BenchmarkTags.NAV_SETTINGS))?.click()
    waitForTag(BenchmarkTags.SETTINGS_LIST)
}

internal fun MacrobenchmarkScope.flingSettings() {
    device.findObject(tagSelector(BenchmarkTags.NAV_SETTINGS))?.click()
    waitForTag(BenchmarkTags.SETTINGS_LIST)
    val settings = device.findObject(tagSelector(BenchmarkTags.SETTINGS_LIST))
    settings.setGestureMargin(device.displayWidth / 5)
    settings.fling(Direction.DOWN)
}

private fun MacrobenchmarkScope.waitForTag(tag: String) {
    check(device.wait(Until.hasObject(tagSelector(tag)), TIMEOUT_MS)) {
        "Timed out waiting for $tag"
    }
}
