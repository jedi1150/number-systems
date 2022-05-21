package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationResult(from: NumberSystem, toRadix: Radix) {
    val arrowRightId = "arrowRight"
    val text = buildAnnotatedString {
        append(numberSystem(from))
        appendInlineContent(arrowRightId, "→")
//        append(numberSystem(toRadix))
    }
    val inlineContent = mapOf(
        Pair(
            arrowRightId,
            InlineTextContent(
                Placeholder(
                    width = 12.sp,
                    height = 12.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                )
            ) {
                Icon(painterResource(R.drawable.arrow_right), null)
            }
        )
    )

    Column {
        Text(
            text = stringResource(id = R.string.explanation_result),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = text,
            inlineContent = inlineContent,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewExplanationResult() {
    NumberSystemsTheme {
        Surface {
            ExplanationResult(NumberSystem("256", Radix(10)), Radix(2))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationResultDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ExplanationResult(NumberSystem("256", Radix(10)), Radix(2))
        }
    }
}
