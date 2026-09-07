package ru.sandello.binaryconverter.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun coldStartupWithoutCompilation() = coldStartup(CompilationMode.None())

    @Test
    fun coldStartupWithBaselineProfile() = coldStartup(
        CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
    )

    private fun coldStartup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = targetPackageName(),
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        iterations = BENCHMARK_ITERATIONS,
        startupMode = StartupMode.COLD,
        setupBlock = { pressHome() },
    ) {
        startActivityAndWaitForContent()
    }
}
