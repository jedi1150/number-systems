package ru.sandello.binaryconverter.ui.calculator

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.PhoneLightDarkPreviews
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun CalculatorEmptyScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            CalculatorScreen(
                calculatorUiState = CalculatorUiState(),
                isDigitGroupingEnabled = true,
                onNumberSystemChange = { _, _ -> },
                onRadixChange = { _, _ -> },
                onArithmeticChange = {},
            )
        }
    }
}

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun CalculatorFilledScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            CalculatorScreen(
                calculatorUiState = CalculatorUiState(
                    numberSystemCustom1 = NumberSystem("10", Radix.DEC),
                    numberSystemCustom2 = NumberSystem("101", Radix.BIN),
                    numberSystemResult = NumberSystem("15", Radix.DEC),
                    selectedArithmetic = ArithmeticType.Addition,
                ),
                isDigitGroupingEnabled = true,
                onNumberSystemChange = { _, _ -> },
                onRadixChange = { _, _ -> },
                onArithmeticChange = {},
            )
        }
    }
}
