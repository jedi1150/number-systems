package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.LongDivision
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ExplanationDivision(from: NumberSystem, to: NumberSystem) {

    val longDivisionList: MutableList<LongDivision> = mutableListOf()
    do {
        if (longDivisionList.isEmpty()) {
            longDivisionList.add(longDivision(dividend = from.value.toBigDecimal(), divisor = to.radix.toBigDecimal()))
        } else {
            longDivisionList.add(longDivision(dividend = longDivisionList.last().quotient, divisor = longDivisionList.last().divisor))
        }
    } while (longDivisionList.last().quotient >= to.radix.toBigDecimal())

    Column {
        Text(
            text = stringResource(id = R.string.explanation_perform_division),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(longDivisionList) { longDivision ->
                    ExplanationLongDivision(longDivision)
                }
            },
        )

        Text(
            text = stringResource(id = R.string.explanation_reverse_result),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                item {
                    NumberSystem(numberSystem = to)
                }
            }
        )
    }

}

private fun longDivision(dividend: BigDecimal, divisor: BigDecimal): LongDivision {
    val quotient: BigDecimal = dividend.divide(divisor, RoundingMode.FLOOR)
    val division: BigDecimal = quotient.multiply(divisor)
    val remainder: BigDecimal = dividend.subtract(division)

    return LongDivision(
        dividend = dividend,
        divisor = divisor,
        quotient = quotient,
        division = division,
        remainder = remainder,
    )
}

@Preview
@Composable
private fun PreviewExplanationDivision() {
    NumberSystemsTheme {
        Surface {
            ExplanationDivision(NumberSystem("256", 10), NumberSystem("1 000 000", 2))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationDivisionDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationDivision(NumberSystem("256", 10), NumberSystem("1 000 000", 2))
        }
    }
}
