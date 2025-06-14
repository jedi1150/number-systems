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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.getFractional

@Composable
fun ExplanationAddIntegerAndFractionalContent(to: NumberSystem, isDigitGroupingEnabled: Boolean) {
    val integerPart: String = to.value.substringBefore(NS_DELIMITER)
    val fractionalPart: String = getFractional(to.value)

    val decimalInteger = NumberSystem(value = integerPart, radix = to.radix)
    val decimalFractional = NumberSystem(value = fractionalPart, radix = to.radix)

    Column(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExplanationDescription(stringResource(id = R.string.explanation_convert_combine_integer_and_fractional_parts))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    append(numberSystem(numberSystem = decimalInteger, isDigitGroupingEnabled = isDigitGroupingEnabled))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) {
                        append("+")
                    }
                    append(numberSystem(numberSystem = decimalFractional, isDigitGroupingEnabled = isDigitGroupingEnabled))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) {
                        append("=")
                    }
                    append(numberSystem(numberSystem = to, isDigitGroupingEnabled = isDigitGroupingEnabled))
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
fun PreviewExplanationCombineParts() {
    NumberSystemsTheme {
        Surface {
            ExplanationAddIntegerAndFractionalContent(to = NumberSystem(value = "A.B4", Radix.HEX), isDigitGroupingEnabled = true)
        }
    }
}
