package ru.sandello.binaryconverter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import ru.sandello.binaryconverter.utils.AppLog

@OptIn(ExperimentalCoroutinesApi::class)
open class MainDispatcherTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUpMainDispatcher() {
        AppLog.delegate = AppLog.NoOp
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDownMainDispatcher() {
        Dispatchers.resetMain()
        AppLog.delegate = AppLog.Android
    }
}
