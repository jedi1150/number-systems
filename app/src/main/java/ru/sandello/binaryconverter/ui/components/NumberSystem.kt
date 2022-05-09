package ru.sandello.binaryconverter.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun NumberSystem(value: String, radix: Int) {
    Text(
        text = buildAnnotatedString {
            append(value)
            withStyle(style = MaterialTheme.typography.overline.toSpanStyle().copy(baselineShift = BaselineShift.Subscript)) {
                append(radix.toString())
            }
        },
    )
}

@Preview
@Composable
private fun PreviewNumberSystem() {
    NumberSystemsTheme {
        Surface {
            NumberSystem(value = "256", radix = 10)
        }
    }
}

@Preview
@Composable
private fun PreviewNumberSystemDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            NumberSystem(value = "256", radix = 10)
        }
    }
}
