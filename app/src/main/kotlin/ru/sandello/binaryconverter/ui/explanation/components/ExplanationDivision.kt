package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.Division
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.pretty

@Composable
fun ExplanationDivision(division: Division) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontFamily = RobotoMonoFamily)) {
                append(division.dividend.toString().pretty(Radix.DEC))
                withStyle(SpanStyle(letterSpacing = 4.sp)) {
                    append("÷")
                }
                append(division.divisor.toString())
                withStyle(SpanStyle(letterSpacing = 6.sp)) {
                    append("=")
                }
                append(division.quotient.toString().pretty(Radix.DEC))
            }
            append(", ")
            append(stringResource(R.string.remainder))
            append(": ")
            withStyle(SpanStyle(fontFamily = RobotoMonoFamily)) {
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(division.remainder.toString())
                }
                division.convertedRemainder?.let { convertedRemainder ->
                    withStyle(SpanStyle(letterSpacing = 6.sp)) {
                        append("=")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(convertedRemainder.uppercase())
                    }
                }
            }
        },
        style = MaterialTheme.typography.bodyMedium,
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
