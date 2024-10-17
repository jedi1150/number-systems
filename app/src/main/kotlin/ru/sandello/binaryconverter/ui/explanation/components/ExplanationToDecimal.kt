package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationToDecimal(from: NumberSystem) {
    Column {
        ExplanationTitle(stringResource(id = R.string.explanation_convert_to_decimal))
        ExplanationToDecimalContent(from = from)
    }
}

@Preview
@Composable
private fun ExplanationToDecimalPreview() {
    NumberSystemsTheme {
        Surface {
            ExplanationToDecimal(from = NumberSystem("1024", Radix.DEC))
        }
    }
}
