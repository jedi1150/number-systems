package ru.sandello.binaryconverter.ui.explanation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationIntegerDivisionContent
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationTitle
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationInteger(from: NumberSystem, to: NumberSystem, isDigitGroupingEnabled: Boolean) {
    Column {
        ExplanationTitle(stringResource(R.string.integer_part))
        ExplanationIntegerDivisionContent(from = from, to = to, isDigitGroupingEnabled = isDigitGroupingEnabled)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationInteger() {
    NumberSystemsTheme {
        Surface {
            ExplanationInteger(from = NumberSystem(value = "10.5", Radix.DEC), to = NumberSystem(value = "1010.1", Radix.BIN), isDigitGroupingEnabled = true)
        }
    }
}
