package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationResult(from: NumberSystem, to: NumberSystem, isDigitGroupingEnabled: Boolean) {
    val arrowRightId = "arrowRight"
    val text = buildAnnotatedString {
        append(numberSystem(from, isDigitGroupingEnabled = isDigitGroupingEnabled))
        appendInlineContent(arrowRightId, "→")
        append(numberSystem(to, isDigitGroupingEnabled = isDigitGroupingEnabled))
    }
    val inlineContent = mapOf(
        Pair(
            arrowRightId,
            InlineTextContent(
                Placeholder(
                    width = 16.sp,
                    height = 16.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                )
            ) {
                Icon(painterResource(R.drawable.ic_arrow_right), null)
            },
        )
    )

    Column(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = text,
                inlineContent = inlineContent,
            )
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview
@Composable
private fun PreviewExplanationResult() {
    NumberSystemsTheme {
        Surface {
            ExplanationResult(NumberSystem("256", Radix.DEC), NumberSystem("100", Radix.BIN), isDigitGroupingEnabled = true)
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationResultDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationResult(NumberSystem("256", Radix.DEC), NumberSystem("100", Radix.BIN), isDigitGroupingEnabled = true)
        }
    }
}
