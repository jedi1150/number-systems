package ru.sandello.binaryconverter.ui.explanation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationResult
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationToDecimal
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExplanationContent(from: NumberSystem, to: NumberSystem) {
    val showToDecimal = from.radix != Radix.DEC
    val showFromDecimal = to.radix != Radix.DEC
    val delimiterExists = from.value.contains(NS_DELIMITER)
    val showFromDecimalWithDelimiter = to.radix != Radix.DEC || delimiterExists

    LazyColumn(
        contentPadding = PaddingValues(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        stickyHeader {
            Surface {
                ExplanationResult(from = from, to = to)
            }
        }
        if (showToDecimal) {
            item {
                ExplanationToDecimal(from = from)
            }
        }
        if (showFromDecimalWithDelimiter) {
            if (showToDecimal) {
                item {
                    Divider()
                }
            }
            if (showFromDecimal) {
                item {
                    ExplanationInteger(from, to)
                }
            }
            if (delimiterExists) {
                item {
                    Divider()
                }
                item {
                    ExplanationFractional(from, to)
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationContent(
    @PreviewParameter(NumberSystemFromPreviewParameterProvider::class) numberSystem: NumberSystem,
) {
    NumberSystemsTheme {
        Surface {
            ExplanationContent(from = numberSystem, to = NumberSystem(value = "A.MI", Radix(36)))
        }
    }
}

private class NumberSystemFromPreviewParameterProvider : PreviewParameterProvider<NumberSystem> {
    override val values = sequenceOf(
        NumberSystem("1100.101", Radix.BIN),
        NumberSystem("12.5", Radix.OCT),
        NumberSystem("10.625", Radix.DEC),
        NumberSystem("A.A", Radix.HEX),
    )
}
