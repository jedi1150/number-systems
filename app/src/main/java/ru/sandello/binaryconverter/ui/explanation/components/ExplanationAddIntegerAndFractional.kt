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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.getFractional

@Composable
fun ExplanationAddIntegerAndFractional(to: NumberSystem) {
    val integerPart: String = to.value.substringBefore(NS_DELIMITER)
    val fractionalPart: String = getFractional(to.value)

    val decimalInteger = NumberSystem(value = integerPart, radix = to.radix)
    val decimalFractional = NumberSystem(value = fractionalPart, radix = to.radix)

    Column(
        modifier = Modifier.padding(vertical = 8.dp),
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
                    append(numberSystem(numberSystem = decimalInteger))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) {
                        append("+")
                    }
                    append(numberSystem(numberSystem = decimalFractional))
                    withStyle(SpanStyle(letterSpacing = 6.sp)) {
                        append("=")
                    }
                    append(numberSystem(numberSystem = to))
                },
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
            ExplanationAddIntegerAndFractional(to = NumberSystem(value = "A.B4", Radix.HEX))
        }
    }
}
