package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.model.Division
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationDivision(division: Division) {
    Text(
        text = buildAnnotatedString {
            append(division.dividend.toString())
            withStyle(SpanStyle(letterSpacing = 4.sp)) {
                append("÷")
            }
            append(division.divisor.toString())
            withStyle(SpanStyle(letterSpacing = 6.sp)) {
                append("=")
            }
            append(division.quotient.toString())
            append(", remainder: ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(division.remainder.toString())
            }
            division.convertedRemainder?.let { convertedRemainder ->
                withStyle(SpanStyle(letterSpacing = 6.sp)) {
                    append("=")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(convertedRemainder.uppercase())
                }
            }
        },
    )
}

@Preview
@Composable
private fun PreviewExplanationLongDivision() {
    NumberSystemsTheme {
        Surface {
            ExplanationDivision(
                Division(
                    dividend = 256.toBigDecimal(),
                    divisor = 2,
                    quotient = 128.toBigDecimal(),
                    remainder = 0.toBigDecimal(),
                )
            )
        }
    }
}


@Preview
@Composable
private fun PreviewExplanationLongDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationDivision(
                Division(
                    dividend = 256.toBigDecimal(),
                    divisor = 2,
                    quotient = 128.toBigDecimal(),
                    remainder = 0.toBigDecimal(),
                )
            )
        }
    }
}
