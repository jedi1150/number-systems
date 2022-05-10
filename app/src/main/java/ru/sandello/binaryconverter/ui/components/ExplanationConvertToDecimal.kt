package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationConvertToDecimal(from: NumberSystem, to: NumberSystem) {
    Column {
        Text(
            text = stringResource(id = R.string.explanation_convert_to_decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = buildAnnotatedString {
                append(numberSystem(numberSystem = from))
                withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                from.value.toList().forEachIndexed { index, number ->
                    append(number)
                    withStyle(SpanStyle(letterSpacing = 4.sp)) { append("×") }
                    append(positionedNumber(number = from.radix, position = from.value.length - 1 - index))
                    if (index != from.value.lastIndex) withStyle(SpanStyle(letterSpacing = 6.sp)) { append("+") }
                }
                withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                append(numberSystem(numberSystem = to))
            },
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Preview
@Composable
fun PreviewExplanationConvertToDecimal() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertToDecimal(NumberSystem("256", 8), NumberSystem("174", 10))
        }
    }
}

@Preview
@Composable
fun PreviewExplanationConvertToDecimalDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationConvertToDecimal(NumberSystem("256", 8), NumberSystem("174", 10))
        }
    }
}
