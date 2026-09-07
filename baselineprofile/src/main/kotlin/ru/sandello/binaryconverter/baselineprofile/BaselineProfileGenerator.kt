package ru.sandello.binaryconverter.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun startup() {
        rule.collect(
            packageName = targetPackageName(),
            maxIterations = PROFILE_MAX_ITERATIONS,
            stableIterations = PROFILE_STABLE_ITERATIONS,
            includeInStartupProfile = true,
        ) {
            pressHome()
            startActivityAndWaitForContent()
        }
    }

    @Test
    fun userJourney() {
        rule.collect(
            packageName = targetPackageName(),
            maxIterations = PROFILE_MAX_ITERATIONS,
            stableIterations = PROFILE_STABLE_ITERATIONS,
            includeInStartupProfile = false,
        ) {
            pressHome()
            startActivityAndWait()
            exerciseCriticalUserJourney()
        }
    }
}
