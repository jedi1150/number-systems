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
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.getFractional
import java.math.BigDecimal

@Composable
fun ExplanationConvertFractionalBlock(from: NumberSystem, to: NumberSystem) {
    val scope = rememberCoroutineScope()

    val fromFractional = getFractional(from.value)
    var iterations = 0
    val maxIterations = 12

    val fractionMultiplierList: MutableList<FractionMultiplier> = mutableListOf()

    LaunchedEffect(Unit) {
        scope.launch {
            // TODO (Oleg): inject NumSys ???
            val fromDecimal = NumSys.convert(NumberSystem(fromFractional, from.radix), to.radix).value

            do {
                if (fractionMultiplierList.isEmpty()) {
                    fractionMultiplierList.add(fractionMultiplier(multiplier = fromDecimal, multiplicand = to.radix.value))
                } else {
                    fractionMultiplierList.add(fractionMultiplier(multiplier = getFractional(fractionMultiplierList.last().product), multiplicand = to.radix.value))
                }
                iterations++
            } while (fractionMultiplierList.last().product.toBigDecimal().scale() > 0 && (iterations < fromDecimal.toBigDecimal().scale() || iterations < maxIterations))
        }
    }

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.explanation_convert_fractional),
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
                fractionMultiplierList.forEach { fractionMultiplier ->
                    ExplanationConvertFractional(fractionMultiplier)
                }
            }
        }

        Text(
            text = "Write the result from top to bottom",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                Text(
                    text = buildAnnotatedString {
                        append(numberSystem(numberSystem = NumberSystem(value = fromFractional, radix = from.radix)))
                        withStyle(SpanStyle(letterSpacing = 6.sp)) {
                            append("=")
                        }
                        append(numberSystem(numberSystem = NumberSystem(value = "0." + to.value.substringAfter(NS_DELIMITER), radix = to.radix)))
                    },
                )
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

@Preview
@Composable
private fun PreviewExplanationConvertFractionalBlock() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertFractionalBlock(NumberSystem("10.703125", Radix.DEC), NumberSystem("A.B4", Radix.HEX))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationConvertFractionalBlockDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationConvertFractionalBlock(NumberSystem("10.703125", Radix.DEC), NumberSystem("A.B4", Radix.HEX))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationConvertFractionalBlockDark2() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationConvertFractionalBlock(NumberSystem("123.123", Radix.DEC), NumberSystem("1111011.000111110111", Radix.BIN))
        }
    }
}
