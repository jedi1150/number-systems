package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationContent(fromNumberSystem: NumberSystem, toRadix: Radix) {

    if (fromNumberSystem.value.isBlank()) {
        return
    }

    val scope = rememberCoroutineScope()
    lateinit var decimalResult: NumberSystem
//    LaunchedEffect(Unit) {
//        scope.launch {
//            converter(from = fromNumberSystem, toRadix = toRadix).first().let { convertedData ->
//                decimalResult = convertedData.result
//            }
//        }
//    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        ExplanationResult(from = fromNumberSystem, toRadix = toRadix)
        if (fromNumberSystem.radix.value != 10) {
            ExplanationConvertToDecimal(from = fromNumberSystem)
        }
        if (toRadix.value != 10) {
            ExplanationDivisionBlock(from = fromNumberSystem, toRadix = toRadix)
            if (fromNumberSystem.value.contains(".")) {
                ExplanationConvertFractionalBlock(from = fromNumberSystem, toRadix = toRadix)
                ExplanationCombineParts(from = fromNumberSystem, to = toRadix)
            }
        }
        ExplanationResult(from = fromNumberSystem, toRadix = toRadix)
    }
}

@Preview
@Composable
private fun PreviewExplanationContent() {
    NumberSystemsTheme {
        Surface {
            ExplanationContent(fromNumberSystem = NumberSystem(value = "10.5", Radix(10)), toRadix = Radix(2))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ExplanationContent(fromNumberSystem = NumberSystem(value = "12.55", Radix(8)), toRadix = Radix(10))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentBinHexDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ExplanationContent(fromNumberSystem = NumberSystem(value = "10.101010", Radix(2)), toRadix = Radix(16))
        }
    }
}