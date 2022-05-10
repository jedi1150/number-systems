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
fun PositionedNumber(number: Int, position: Int) {
    Text(
        text = buildAnnotatedString {
            append(number.toString())
            withStyle(style = MaterialTheme.typography.overline.toSpanStyle().copy(baselineShift = BaselineShift.Superscript)) {
                append(position.toString())
            }
        },
    )
}

@Preview
@Composable
private fun PreviewPositionedNumber() {
    NumberSystemsTheme {
        Surface {
            PositionedNumber(number = 5, position = 2)
        }
    }
}

@Preview
@Composable
private fun PreviewPositionedNumberDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            PositionedNumber(number = 5, position = 2)
        }
    }
}
