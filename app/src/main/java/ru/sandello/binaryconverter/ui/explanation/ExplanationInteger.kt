package ru.sandello.binaryconverter.ui.explanation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationIntegerDivision
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationIntegerToDecimal
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationTitle
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationInteger(from: NumberSystem, to: NumberSystem) {
    Column {
        ExplanationTitle("Integer part")
        if (from.radix != Radix.DEC) {
            ExplanationIntegerToDecimal(from = from)
        }
        if (to.radix != Radix.DEC) {
            ExplanationIntegerDivision(from = from, to = to)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationInteger() {
    NumberSystemsTheme {
        Surface {
            ExplanationInteger(from = NumberSystem(value = "10.5", Radix.DEC), to = NumberSystem(value = "1010.1", Radix.BIN))
        }
    }
}
