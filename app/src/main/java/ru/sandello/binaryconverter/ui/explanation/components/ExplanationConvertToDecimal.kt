package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ExplanationConvertToDecimal(from: NumberSystem, to: NumberSystem) {
    val position = from.value.substringBefore(NS_DELIMITER).length
    val filteredValue = from.value.toList().filterNot { it == NS_DELIMITER }

    Column {
        Text(
            text = stringResource(id = R.string.explanation_convert_to_decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(numberSystem(numberSystem = from))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    filteredValue.forEachIndexed { index, number ->
                        append(number)
                        withStyle(SpanStyle(letterSpacing = 4.sp)) { append("×") }
                        append(positionedNumber(number = from.radix.value, position = position - 1 - index))
                        if (index != filteredValue.lastIndex) withStyle(SpanStyle(letterSpacing = 6.sp)) { append("+") }
                    }
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    append(numberSystem(numberSystem = to))
                },
            )
        }
    }
}

@Preview
@Composable
fun PreviewExplanationConvertToDecimal() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertToDecimal(from = NumberSystem("12.55", Radix(8)), to = NumberSystem("10.703125", Radix(10)))
        }
    }
}

@Preview
@Composable
fun PreviewExplanationConvertToDecimalDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationConvertToDecimal(NumberSystem("12.55", Radix(8)), to = NumberSystem("10.703125", Radix(10)))
        }
    }
}