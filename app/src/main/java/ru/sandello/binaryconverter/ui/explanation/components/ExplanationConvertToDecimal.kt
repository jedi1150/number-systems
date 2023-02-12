package ru.sandello.binaryconverter.ui.explanation.components

import android.content.res.Configuration
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
import numsys.NumSys
import numsys.NumSys.toRadix
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ExplanationConvertToDecimal(from: NumberSystem) {
    val integerPart: NumberSystem = from.copy(value = from.value.substringBefore(NS_DELIMITER))

    val position = integerPart.value.substringBefore(NS_DELIMITER).length
    val filteredValue = integerPart.value.toList().filterNot { char -> char == NS_DELIMITER }

    val decimalValue = integerPart.toRadix(Radix.DEC)

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.explanation_convert_to_decimal),
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
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(numberSystem(numberSystem = integerPart))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    filteredValue.forEachIndexed { index, value ->
                        append(value)
                        if (value.isLetter()) {
                            val decimalNumber = NumSys.convert(NumberSystem(value = value.toString(), radix = integerPart.radix), toRadix = Radix.DEC).value
                            append("($decimalNumber)")
                        }
                        withStyle(SpanStyle(letterSpacing = 4.sp)) { append("×") }
                        append(positionedNumber(number = integerPart.radix.value, position = position - 1 - index))
                        if (index != filteredValue.lastIndex) withStyle(SpanStyle(letterSpacing = 6.sp)) { append("+") }
                    }
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    append(numberSystem(numberSystem = decimalValue))
                },
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewExplanationConvertToDecimal() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertToDecimal(from = NumberSystem("12.55", Radix.OCT))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewExplanationConvertHexToDec() {
    NumberSystemsTheme {
        Surface {
            ExplanationConvertToDecimal(NumberSystem("D4.D4", Radix.HEX))
        }
    }
}
