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
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily

@Composable
fun positionedNumber(number: Int, position: Int): AnnotatedString {
    return buildAnnotatedString {
        append(number.toString())
        withStyle(style = MaterialTheme.typography.labelMedium.copy(fontFamily = RobotoMonoFamily, baselineShift = BaselineShift.Superscript).toSpanStyle()) {
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
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            Text(text = positionedNumber(number = 5, position = 2))
        }
    }
}
