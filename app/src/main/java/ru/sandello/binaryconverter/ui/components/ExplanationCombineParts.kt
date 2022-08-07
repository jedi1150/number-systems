package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationCombineParts(from: NumberSystem, to: NumberSystem) {
    Column {
        Text(
            text = stringResource(id = R.string.explanation_convert_combine_decimal_fractional),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                Text(
                    text = buildAnnotatedString {
                        append(numberSystem(numberSystem = from))
                        withStyle(SpanStyle(letterSpacing = 6.sp)) {
                            append("=")
                        }
                        append(numberSystem(numberSystem = to))
                    }
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewExplanationCombineParts() {
    NumberSystemsTheme {
        Surface {
            ExplanationCombineParts(from = NumberSystem(value = "12.55", radix = Radix(8)), to = NumberSystem(value = "A.B4", Radix(16)))
        }
    }
}

@Preview
@Composable
fun PreviewExplanationCombinePartsDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationCombineParts(from = NumberSystem(value = "12.55", radix = Radix(8)), to = NumberSystem(value = "A.B4", Radix(16)))
        }
    }
}
