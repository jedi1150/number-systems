package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.getFractional
import java.math.BigDecimal

@Composable
fun ExplanationConvertFractionalBlock(from: NumberSystem, toRadix: Radix) {
    val fromFractional = getFractional(from.value)
    var iterations = 0

    val fractionMultiplierList: MutableList<FractionMultiplier> = mutableListOf()
    do {
        if (fractionMultiplierList.isEmpty()) {
            fractionMultiplierList.add(fractionMultiplier(multiplier = fromFractional, multiplicand = toRadix.value))
        } else {
            fractionMultiplierList.add(fractionMultiplier(multiplier = getFractional(fractionMultiplierList.last().product), multiplicand = toRadix.value))
        }
        iterations++
    } while (fractionMultiplierList.last().product.toBigDecimal().scale() > 0
        && iterations < fromFractional.toBigDecimal().scale()
    )

    Column {
        Text(
            text = stringResource(id = R.string.explanation_convert_fractional),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        fractionMultiplierList.forEach { fractionMultiplier ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    ExplanationConvertFractional(fractionMultiplier)
                },
            )
        }
        Text(
            text = "Write the result from top to bottom",
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
                        append(numberSystem(numberSystem = NumberSystem(value = fromFractional, radix = from.radix)))
                        withStyle(SpanStyle(letterSpacing = 6.sp)) {
                            append("=")
                        }
//                        append(numberSystem(numberSystem = NumberSystem(value = "0." + toRadix.value.substringAfter("."), radix = toRadix)))
                    }
                )
            }
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
            ExplanationConvertFractionalBlock(NumberSystem("10.703125", Radix(10)), Radix(16))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationConvertFractionalBlockDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ExplanationConvertFractionalBlock(NumberSystem("10.703125", Radix(10)), Radix(16))
        }
    }
}
