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
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.getFractional
import java.math.BigDecimal

@Composable
fun ExplanationFractionalMultiplierContent(from: NumberSystem, to: NumberSystem) {
    val fromFractional = getFractional(from.value)
    var iterations = 0
    val maxIterations = 12

    val fractionMultiplierList: MutableList<FractionMultiplier> = mutableListOf()

    do {
        if (fractionMultiplierList.isEmpty()) {
            fractionMultiplierList.add(fractionMultiplier(multiplier = getFractional(NumberSystem(fromFractional, from.radix).toRadix(Radix.DEC).value), multiplicand = to.radix.value))
        } else {
            fractionMultiplierList.add(fractionMultiplier(multiplier = getFractional(fractionMultiplierList.last().product), multiplicand = to.radix.value))
        }
        iterations++
    } while (fractionMultiplierList.last().product.toBigDecimal().scale() > 0 && (iterations < getFractional(NumberSystem(fromFractional, from.radix).toRadix(Radix.DEC).value).toBigDecimal().scale() && iterations < maxIterations))

    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExplanationDescription(stringResource(id = R.string.explanation_convert_fractional))

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
                fractionMultiplierList.forEach { fractionMultiplier ->
                    ExplanationConvertFractional(fractionMultiplier)
                }
            }
        }

        ExplanationDescription(text = stringResource(id = R.string.explanation_convert_fractional_write_result))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                Text(text = numberSystem(numberSystem = NumberSystem(value = "0." + to.value.substringAfter(NS_DELIMITER), radix = to.radix)))
            },
        )
    }
}

private fun fractionMultiplier(multiplier: String, multiplicand: Int): FractionMultiplier {
    val product: BigDecimal = multiplier.toBigDecimal().multiply(multiplicand.toBigDecimal()).stripTrailingZeros()
    val convertedProduct: String? = if (product.toBigInteger() > 10.toBigInteger()) product.toBigInteger().toString(multiplicand).uppercase() else null

    return FractionMultiplier(
        multiplier = multiplier,
        multiplicand = multiplicand,
        product = product.toString(),
        convertedProduct = convertedProduct,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationFractionalMultiplier() {
    NumberSystemsTheme {
        Surface {
            ExplanationFractionalMultiplierContent(NumberSystem("10.703125", Radix.DEC), NumberSystem("A.B4", Radix.HEX))
        }
    }
}

