package ru.sandello.binaryconverter.ui.explanation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.explanation.components.*
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ExplanationContent(from: NumberSystem, to: NumberSystem) {

    if (from.value.isBlank()) {
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        item {
            ExplanationResult(from = from, to = to)
        }
        if (from.radix != Radix.DEC) {
            item {
                ExplanationConvertToDecimal(from = from)
            }
        }
        if (to.radix != Radix.DEC) {
            item {
                ExplanationDivisionBlock(from = from, to = to)
            }
            if (from.value.contains(NS_DELIMITER)) {
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
            ExplanationContent(from = NumberSystem(value = "10.5", Radix.DEC), to = NumberSystem(value = "1010.1", Radix.BIN))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationContent(from = NumberSystem(value = "12.55", Radix.OCT), to = NumberSystem(value = "10.703125", Radix.DEC))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationContentBinHexDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationContent(from = NumberSystem(value = "10.101010", Radix.BIN), to = NumberSystem(value = "2.A8", Radix.HEX))
        }
    }
}