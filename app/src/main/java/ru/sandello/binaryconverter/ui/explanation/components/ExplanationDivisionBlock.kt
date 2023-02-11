package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import numsys.NumSys
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.Division
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ExplanationDivisionBlock(from: NumberSystem, to: NumberSystem) {
    val scope = rememberCoroutineScope()

    val fromDecimal = from.value.substringBefore(NS_DELIMITER)

    if (fromDecimal.isBlank()) return

    val divisionList: MutableList<Division> = mutableListOf()

    LaunchedEffect(Unit) {
        scope.launch {
            do {
                if (divisionList.isEmpty()) {
                    // TODO (Oleg): inject NymSys ???
                    divisionList.add(longDivision(dividend = NumSys.convert(NumberSystem(fromDecimal, from.radix), to.radix).value.toBigDecimal(), divisor = to.radix.value))
                } else {
                    divisionList.add(longDivision(dividend = divisionList.last().quotient, divisor = divisionList.last().divisor))
                }
            } while (divisionList.last().quotient > BigDecimal("0"))
        }
    }

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.explanation_perform_division),
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
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                divisionList.forEach { longDivision ->
                    ExplanationDivision(longDivision)
                }
            }
        }

        Text(
            text = stringResource(id = R.string.explanation_reverse_result),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            content = {
                Text(
                    text = buildAnnotatedString {
                        append(numberSystem(numberSystem = NumberSystem(value = fromDecimal, radix = from.radix)))
                        withStyle(SpanStyle(letterSpacing = 6.sp)) {
                            append("=")
                        }
                        append(numberSystem(numberSystem = NumberSystem(value = to.value.substringBefore(NS_DELIMITER), radix = to.radix)))
                    },
                )
            },
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
            ExplanationDivisionBlock(NumberSystem("10.5", Radix.DEC), NumberSystem("1010.1", Radix.BIN))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationDivisionBlock(NumberSystem("25", Radix.DEC), NumberSystem("11001", Radix.BIN))
        }
    }
}
