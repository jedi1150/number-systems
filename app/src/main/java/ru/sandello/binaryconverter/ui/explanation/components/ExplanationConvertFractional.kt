package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ExplanationConvertFractional(fractionMultiplier: FractionMultiplier) {
    Text(
        text = buildAnnotatedString {
            append(fractionMultiplier.multiplier)
            withStyle(SpanStyle(letterSpacing = 4.sp)) {
                append("×")
            }
            append(fractionMultiplier.multiplicand.toString())
            withStyle(SpanStyle(letterSpacing = 6.sp)) {
                append("=")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(fractionMultiplier.product.substringBefore(NS_DELIMITER))
            }
            if (fractionMultiplier.product.contains(NS_DELIMITER)) {
                append(NS_DELIMITER)
                append(fractionMultiplier.product.substringAfter(NS_DELIMITER))
            }
            fractionMultiplier.convertedProduct?.let { convertedProduct ->
                withStyle(SpanStyle(letterSpacing = 6.sp)) {
                    append("=")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(convertedProduct.uppercase())
                }
            }
        },
    )
}

@Preview
@Composable
private fun PreviewExplanationConvertFractional() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertFractional(FractionMultiplier(multiplier = "10.703125", multiplicand = 16, product = "11.25", convertedProduct = "B"))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationConvertFractionalDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationConvertFractional(FractionMultiplier(multiplier = "10.703125", multiplicand = 16, product = "11.25", convertedProduct = "B"))
        }
    }
}
