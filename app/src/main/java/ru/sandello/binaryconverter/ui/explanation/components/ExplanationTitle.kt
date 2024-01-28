package ru.sandello.binaryconverter.ui.explanation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ExplanationTitle(
    text: String,
    contentPaddingValues: PaddingValues = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPaddingValues),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExplanationTitle() {
    NumberSystemsTheme {
        Surface {
            ExplanationTitle("Title")
        }
    }
}
