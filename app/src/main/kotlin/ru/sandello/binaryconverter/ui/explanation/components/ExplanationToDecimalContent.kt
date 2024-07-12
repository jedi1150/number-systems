package ru.sandello.binaryconverter.ui.explanation.components

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import numsys.NumSys
import numsys.NumSys.toRadix
import numsys.model.Radix
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.asExternalModel
import ru.sandello.binaryconverter.model.asInternalModel
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoFamily
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ExplanationToDecimalContent(from: NumberSystem) {
    val integerPart = NumberSystem(value = from.value.substringBefore(NS_DELIMITER), from.radix)

    val position = from.value.substringBefore(NS_DELIMITER).length
    val filteredValue = from.value.toList().filterNot { char -> char == NS_DELIMITER }

    val result = NumSys.convert(numberSystem = from.asInternalModel(), targetRadix = Radix.DEC)

    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(numberSystem(numberSystem = from))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    filteredValue.forEachIndexed { index, value ->
                        append(value)
                        if (value.isLetter()) {
                            val decimalNumber = NumberSystem(value = value.toString(), radix = integerPart.radix).asInternalModel().toRadix(Radix.DEC).value
                            withStyle(SpanStyle(fontFamily = RobotoFamily)) {
                                append("(")
                            }
                            append(decimalNumber)
                            withStyle(SpanStyle(fontFamily = RobotoFamily)) {
                                append(")")
                            }
                        }
                        withStyle(SpanStyle(letterSpacing = 4.sp)) { append("×") }
                        append(positionedNumber(number = integerPart.radix.value, position = position - 1 - index))
                        if (index != filteredValue.lastIndex) withStyle(SpanStyle(letterSpacing = 6.sp)) { append("+") }
                    }
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("=") }
                    append(numberSystem(numberSystem = result.asExternalModel()))
                },
                fontFamily = RobotoMonoFamily,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationToDecimal(
    @PreviewParameter(NumberSystemPreviewParameterProvider::class) numberSystem: NumberSystem,
) {
    NumberSystemsTheme {
        Surface {
            ExplanationToDecimalContent(from = numberSystem)
        }
    }
}

private class NumberSystemPreviewParameterProvider : PreviewParameterProvider<NumberSystem> {
    override val values = sequenceOf(
        NumberSystem("12.55", Radix.OCT),
        NumberSystem("D4.D4", Radix.HEX),
    )
}
