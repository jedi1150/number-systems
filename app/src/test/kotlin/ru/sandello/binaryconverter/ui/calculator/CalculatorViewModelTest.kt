package ru.sandello.binaryconverter.ui.calculator

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.sandello.binaryconverter.MainDispatcherTest
import ru.sandello.binaryconverter.numsys.NumSys
import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.repository.FakeSettingsRepository

class CalculatorViewModelTest : MainDispatcherTest() {
    private fun viewModel() = CalculatorViewModel(
        numSys = NumSys,
        settingsRepository = FakeSettingsRepository(),
    )

    @Test
    fun convertFrom_addsOperands() = runTest {
        val viewModel = viewModel()
        viewModel.updateRadix(CalculatorRadixType.RadixResult, Radix.DEC)

        viewModel.convertFrom(CalculatorOperandType.OperandCustom1, NumberSystem("10", Radix.DEC))
        viewModel.convertFrom(CalculatorOperandType.OperandCustom2, NumberSystem("101", Radix.BIN))

        assertEquals("15", viewModel.calculatorUiState.value.numberSystemResult.value)
    }

    @Test
    fun convertFrom_invalidCharacter_marksOperandError() = runTest {
        val viewModel = viewModel()

        viewModel.convertFrom(CalculatorOperandType.OperandCustom1, NumberSystem("2", Radix.BIN))

        assertTrue(viewModel.calculatorUiState.value.numberSystemCustom1Error)
    }

    @Test
    fun clear_resetsOperands() = runTest {
        val viewModel = viewModel()
        viewModel.convertFrom(CalculatorOperandType.OperandCustom1, NumberSystem("10", Radix.DEC))

        viewModel.clear()

        assertEquals("", viewModel.calculatorUiState.value.numberSystemCustom1.value)
        assertEquals("", viewModel.calculatorUiState.value.numberSystemResult.value)
    }
}
