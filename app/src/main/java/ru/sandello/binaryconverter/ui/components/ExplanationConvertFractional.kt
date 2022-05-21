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
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

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
                append(fractionMultiplier.product.substringBefore("."))
            }
            if (fractionMultiplier.product.contains(".")) {
                append(".")
                append(fractionMultiplier.product.substringAfter("."))
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
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ExplanationConvertFractional(FractionMultiplier(multiplier = "10.703125", multiplicand = 16, product = "11.25", convertedProduct = "B"))
        }
    }
}
