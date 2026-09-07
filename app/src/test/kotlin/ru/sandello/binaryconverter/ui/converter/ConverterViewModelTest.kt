package ru.sandello.binaryconverter.ui.converter

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.sandello.binaryconverter.MainDispatcherTest
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.NumSys
import ru.sandello.binaryconverter.numsys.NumberSystemDataSource
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.repository.FakeSettingsRepository
import ru.sandello.binaryconverter.repository.NumberSystemRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ConverterViewModelTest : MainDispatcherTest() {
    private fun viewModel() = ConverterViewModel(
        numberSystemRepository = NumberSystemRepository(
            NumberSystemDataSource(NumSys, UnconfinedTestDispatcher()),
        ),
        settingsRepository = FakeSettingsRepository(),
    )

    @Test
    fun convertFrom_decimalValue_fillsOtherRadixes() = runTest {
        val viewModel = viewModel()

        viewModel.convertFrom(NumberSystem("255", Radix.DEC))

        val state = viewModel.converterUiState.value
        assertEquals("255", state.numberSystem10.value)
        assertEquals("11111111", state.numberSystem2.value)
        assertEquals("377", state.numberSystem8.value)
        assertEquals("FF", state.numberSystem16.value.uppercase())
        assertFalse(state.numberSystem10.isError)
    }

    @Test
    fun convertFrom_invalidHexCharacter_marksError() = runTest {
        val viewModel = viewModel()

        viewModel.convertFrom(NumberSystem("G", Radix.HEX))

        assertTrue(viewModel.converterUiState.value.numberSystem16.isError)
    }

    @Test
    fun clear_resetsConvertedValues() = runTest {
        val viewModel = viewModel()
        viewModel.convertFrom(NumberSystem("10", Radix.DEC))

        viewModel.clear()

        val state = viewModel.converterUiState.value
        assertEquals("", state.numberSystem10.value)
        assertEquals("", state.numberSystem2.value)
        assertFalse(state.hasData)
    }
}
