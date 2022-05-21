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
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun positionedNumber(number: Int, position: Int): AnnotatedString {
    return buildAnnotatedString {
        append(number.toString())
        withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(baselineShift = BaselineShift.Superscript)) {
            append(position.toString())
        }
    }
}

@Preview
@Composable
private fun PreviewPositionedNumber() {
    NumberSystemsTheme {
        Surface {
            Text(text = positionedNumber(number = 5, position = 2))
        }
    }
}

@Preview
@Composable
private fun PreviewPositionedNumberDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            Text(text = positionedNumber(number = 5, position = 2))
        }
    }
}
