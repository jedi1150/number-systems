package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import ru.sandello.binaryconverter.model.Division
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ExplanationDivisionBlock(from: NumberSystem, to: NumberSystem) {
    val fromDecimal = from.value.substringBefore(".")
    val toDecimal = to.value.substringBefore(".")

    val divisionList: MutableList<Division> = mutableListOf()
    do {
        if (divisionList.isEmpty()) {
            divisionList.add(longDivision(dividend = fromDecimal.toBigDecimal(), divisor = to.radix))
        } else {
            divisionList.add(longDivision(dividend = divisionList.last().quotient, divisor = divisionList.last().divisor))
        }
    } while (divisionList.last().quotient >= to.radix.toBigDecimal())

    Column {
        Text(
            text = stringResource(id = R.string.explanation_perform_division),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )

        divisionList.forEach { longDivision ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    ExplanationDivision(longDivision)
                }
            )
        }

        Text(
            text = stringResource(id = R.string.explanation_reverse_result),
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
                        append(numberSystem(numberSystem = NumberSystem(value = fromDecimal, radix = from.radix)))
                        withStyle(SpanStyle(letterSpacing = 6.sp)) {
                            append("=")
                        }
                        append(numberSystem(numberSystem = NumberSystem(value = toDecimal, radix = to.radix)))
                    }
                )
            }
        )
    }

}

private fun longDivision(dividend: BigDecimal, divisor: Int): Division {
    val quotient: BigDecimal = dividend.divide(divisor.toBigDecimal(), RoundingMode.FLOOR)
    val remainder: BigDecimal = dividend.subtract(quotient.multiply(divisor.toBigDecimal()))
    val convertedRemainder = if (remainder >= BigDecimal(10)) remainder.toBigInteger().toString(divisor) else null

    return Division(
        dividend = dividend,
        divisor = divisor,
        quotient = quotient,
        remainder = remainder,
        convertedRemainder = convertedRemainder,
    )
}

@Preview
@Composable
private fun PreviewExplanationDivision() {
    NumberSystemsTheme {
        Surface {
            ExplanationDivisionBlock(NumberSystem("14", 10), NumberSystem("E", 16))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationDivisionBlock(NumberSystem("25", 10), NumberSystem("1 000 000", 2))
        }
    }
}
