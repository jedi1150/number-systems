package ru.sandello.binaryconverter.ui.explanation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationResult
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationTitle
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

    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        stickyHeader {
            ExplanationTitle(
                text = stringResource(id = R.string.explanation_result),
                contentPaddingValues = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }
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
                    HorizontalDivider()
                }
            }
            if (showFromDecimal) {
                item {
                    ExplanationInteger(from, to)
                }
            }
            if (delimiterExists) {
                item {
                    HorizontalDivider()
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
