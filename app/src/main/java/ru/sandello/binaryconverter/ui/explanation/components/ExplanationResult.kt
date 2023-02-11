package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationResult(from: NumberSystem, to: NumberSystem) {
    val arrowRightId = "arrowRight"
    val text = buildAnnotatedString {
        append(numberSystem(from))
        appendInlineContent(arrowRightId, "→")
        append(numberSystem(to))
    }
    val inlineContent = mapOf(
        Pair(
            arrowRightId,
            InlineTextContent(
                Placeholder(
                    width = 12.sp,
                    height = 12.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline,
                )
            ) {
                Icon(painterResource(R.drawable.arrow_right), null)
            },
        )
    )

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.explanation_result),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
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
    }
}

@Preview
@Composable
private fun PreviewExplanationResult() {
    NumberSystemsTheme {
        Surface {
            ExplanationResult(NumberSystem("256", Radix.DEC), (NumberSystem("100", Radix.BIN)))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationResultDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationResult(NumberSystem("256", Radix.DEC), (NumberSystem("100", Radix.BIN)))
        }
    }
}
