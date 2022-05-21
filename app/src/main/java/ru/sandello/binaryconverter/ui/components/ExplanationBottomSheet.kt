package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.Shapes

@Composable
fun Explanation(from: NumberSystem, to: Radix) {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Divider(
                modifier = Modifier
                    .size(width = 50.dp, height = 4.dp)
                    .clip(Shapes.small),
            )
        }
        Text(
            text = stringResource(R.string.explanation),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
        ExplanationContent(fromNumberSystem = from, toRadix = to)
    }
}

@Composable
@Preview
private fun PreviewExplanation() {
    NumberSystemsTheme {
        Surface {
            Explanation(from = NumberSystem(value = "10.5", Radix(10)), to = Radix(2))
        }
    }
}

@Composable
@Preview
private fun PreviewExplanationDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            Explanation(from = NumberSystem(value = "10.10", Radix(10)), to = Radix(2))
        }
    }
}

