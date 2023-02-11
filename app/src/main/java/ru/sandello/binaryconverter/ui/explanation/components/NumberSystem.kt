package ru.sandello.binaryconverter.ui.explanation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.NumberSystem
import numsys.model.Radix
import numsys.model.pretty
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun numberSystem(numberSystem: NumberSystem): AnnotatedString {
    return buildAnnotatedString {
        append(numberSystem.pretty())
        withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(baselineShift = BaselineShift.Subscript)) {
            append(numberSystem.radix.value.toString())
        }
    }
}

@Preview
@Composable
private fun PreviewNumberSystem() {
    NumberSystemsTheme {
        Surface {
            Text(text = numberSystem(numberSystem = NumberSystem("1024", Radix.DEC)))
        }
    }
}

@Preview
@Composable
private fun PreviewNumberSystemDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            Text(text = numberSystem(numberSystem = NumberSystem("11100100", Radix.BIN)))
        }
    }
}
