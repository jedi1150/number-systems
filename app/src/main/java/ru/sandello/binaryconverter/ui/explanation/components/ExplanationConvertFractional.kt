package ru.sandello.binaryconverter.ui.explanation.components

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import numsys.model.Radix
import ru.sandello.binaryconverter.model.FractionMultiplier
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.pretty

@Composable
fun ExplanationConvertFractional(fractionMultiplier: FractionMultiplier) {
    Text(
        text = buildAnnotatedString {
            append(fractionMultiplier.multiplier.pretty(Radix.DEC))
            withStyle(SpanStyle(letterSpacing = 4.sp)) {
                append("×")
            }
            append(fractionMultiplier.multiplicand.toString())
            withStyle(SpanStyle(letterSpacing = 6.sp)) {
                append("=")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                append(fractionMultiplier.product.substringBefore(NS_DELIMITER))
            }
            if (fractionMultiplier.product.contains(NS_DELIMITER)) {
                append(NS_DELIMITER)
                append(fractionMultiplier.product.substringAfter(NS_DELIMITER).pretty(Radix.DEC))
            }
            fractionMultiplier.convertedProduct?.let { convertedProduct ->
                withStyle(SpanStyle(letterSpacing = 6.sp)) {
                    append("=")
                }
                withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(convertedProduct.uppercase())
                }
            }
        },
        fontFamily = RobotoMonoFamily,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun PreviewExplanationConvertFractional() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertFractional(FractionMultiplier(multiplier = "0.703125", multiplicand = 16, product = "11.25", convertedProduct = "B"))
        }
    }
}
