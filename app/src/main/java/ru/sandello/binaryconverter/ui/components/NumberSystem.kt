package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun numberSystem(numberSystem: NumberSystem): AnnotatedString {
    return buildAnnotatedString {
        append(numberSystem.value)
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
            Text(text = numberSystem(numberSystem = NumberSystem("256", Radix(10))))
        }
    }
}

@Preview
@Composable
private fun PreviewNumberSystemDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            Text(text = numberSystem(numberSystem = NumberSystem("256", Radix(10))))
        }
    }
}
