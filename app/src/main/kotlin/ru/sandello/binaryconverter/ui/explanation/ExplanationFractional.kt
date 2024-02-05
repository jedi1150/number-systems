package ru.sandello.binaryconverter.ui.explanation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationAddIntegerAndFractionalContent
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationFractionalMultiplierContent
import ru.sandello.binaryconverter.ui.explanation.components.ExplanationTitle
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationFractional(from: NumberSystem, to: NumberSystem) {
    Column {
        ExplanationTitle(stringResource(R.string.fractional_part))
        ExplanationFractionalMultiplierContent(from = from, to = to)
        ExplanationAddIntegerAndFractionalContent(to = to)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationFractional() {
    NumberSystemsTheme {
        Surface {
            ExplanationFractional(from = NumberSystem(value = "10.5", Radix.DEC), to = NumberSystem(value = "1010.1", Radix.BIN))
        }
    }
}
