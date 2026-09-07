package ru.sandello.binaryconverter.ui.converter

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.PhoneLightDarkPreviews
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun ConverterEmptyScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            ConverterScreen(
                converterUiState = ConverterUiState(),
                isDigitGroupingEnabled = true,
                onNumberSystemChanged = {},
                onCustomRadixChanged = {},
            )
        }
    }
}

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun ConverterFilledScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            ConverterScreen(
                converterUiState = ConverterUiState(
                    numberSystem2 = NumberSystem("11111111", Radix.BIN),
                    numberSystem8 = NumberSystem("377", Radix.OCT),
                    numberSystem10 = NumberSystem("255", Radix.DEC),
                    numberSystem16 = NumberSystem("FF", Radix.HEX),
                    numberSystemCustom = NumberSystem("100110", Radix(3)),
                ),
                isDigitGroupingEnabled = true,
                onNumberSystemChanged = {},
                onCustomRadixChanged = {},
            )
        }
    }
}

@PreviewTest
@Preview(
    name = "large font",
    showBackground = true,
    device = "spec:width=411dp,height=891dp",
    fontScale = 1.5f,
)
@Composable
fun ConverterFilledLargeFontScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            ConverterScreen(
                converterUiState = ConverterUiState(
                    numberSystem10 = NumberSystem("1234567", Radix.DEC),
                    numberSystem2 = NumberSystem("100101101011010000111", Radix.BIN),
                    numberSystem8 = NumberSystem("4553207", Radix.OCT),
                    numberSystem16 = NumberSystem("12D687", Radix.HEX),
                ),
                isDigitGroupingEnabled = true,
                onNumberSystemChanged = {},
                onCustomRadixChanged = {},
            )
        }
    }
}

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun ConverterErrorScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            ConverterScreen(
                converterUiState = ConverterUiState(
                    numberSystem16 = NumberSystem("G", Radix.HEX, isError = true),
                ),
                isDigitGroupingEnabled = true,
                onNumberSystemChanged = {},
                onCustomRadixChanged = {},
            )
        }
    }
}
