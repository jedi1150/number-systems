package ru.sandello.binaryconverter.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun settingsScroll() = benchmarkRule.measureRepeated(
        packageName = targetPackageName(),
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = BENCHMARK_ITERATIONS,
        startupMode = StartupMode.WARM,
        setupBlock = { pressHome() },
    ) {
        startActivityAndWaitForContent()
        flingSettings()
    }
}
