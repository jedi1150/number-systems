package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationResult(from: NumberSystem, to: NumberSystem) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NumberSystem(from)
        Icon(
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        NumberSystem(to)
    }
}

@Preview
@Composable
private fun PreviewExplanationResult() {
    NumberSystemsTheme {
        Surface {
            ExplanationResult(NumberSystem("256", 10), NumberSystem("1 000 000", 2))
        }
    }
}

@Preview
@Composable
private fun PreviewExplanationResultDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationResult(NumberSystem("256", 10), NumberSystem("1 000 000", 2))
        }
    }
}
