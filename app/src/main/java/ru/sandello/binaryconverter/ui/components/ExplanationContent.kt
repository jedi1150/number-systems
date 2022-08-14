package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationContent(from: NumberSystem, to: NumberSystem) {

    if (from.value.isBlank()) {
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

    LazyColumn(
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        item {
            ExplanationResult(from = from, to = to)
        }
        if (from.radix.value != 10) {
            item {
                ExplanationConvertToDecimal(from = from, to = to)
            }
        }
        if (to.radix != Radix(10)) {
            item {
                ExplanationDivisionBlock(from = from, to = to)
            }
            if (from.value.contains(".")) {
                item {
                    ExplanationConvertFractionalBlock(from = from, to = to)
                }
                item {
                    ExplanationCombineParts(from = from, to = to)
                }
            }
        }
        item {
            ExplanationResult(from = from, to = to)
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContent() {
    NumberSystemsTheme {
        Surface {
            ExplanationContent(from = NumberSystem(value = "10.5", Radix(10)), to = NumberSystem(value = "1010.1", Radix(2)))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationContent(from = NumberSystem(value = "12.55", Radix(8)), to = NumberSystem(value = "10.703125", Radix(10)))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentBinHexDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationContent(from = NumberSystem(value = "10.101010", Radix(2)), to = NumberSystem(value = "2.A8", Radix(16)))
        }
    }
}