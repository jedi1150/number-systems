package ru.sandello.binaryconverter.ui.explanation.components

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import numsys.NumSys.toRadix
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.Division
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.asInternalModel
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ExplanationIntegerDivisionContent(from: NumberSystem, to: NumberSystem) {
    val fromDecimal = from.value.substringBefore(NS_DELIMITER)
    var iterations = 0
    val maxIterations = 12

    if (fromDecimal.isBlank()) return

    val divisionList: MutableList<Division> = mutableListOf()

    do {
        if (divisionList.isEmpty()) {
            divisionList.add(longDivision(dividend = NumberSystem(fromDecimal, from.radix).asInternalModel().toRadix(Radix.DEC, ignoreCase = true).value.toBigDecimal(), divisor = to.radix.value))
        } else {
            divisionList.add(longDivision(dividend = divisionList.last().quotient, divisor = divisionList.last().divisor))
        }
        iterations++
    } while (divisionList.last().quotient > BigDecimal("0") && iterations < maxIterations)

    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExplanationDescription(stringResource(id = R.string.explanation_integer_division))

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

        ExplanationDescription(stringResource(id = R.string.explanation_reverse_result))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(text = numberSystem(numberSystem = NumberSystem(value = to.value.substringBefore(NS_DELIMITER), radix = to.radix)))
        }
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewExplanationIntegerDivision() {
    NumberSystemsTheme {
        Surface {
            ExplanationIntegerDivisionContent(NumberSystem("10.5", Radix.DEC), NumberSystem("1010.1", Radix.BIN))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationIntegerDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationIntegerDivisionContent(NumberSystem("25", Radix.DEC), NumberSystem("11001", Radix.BIN))
        }
    }
}
