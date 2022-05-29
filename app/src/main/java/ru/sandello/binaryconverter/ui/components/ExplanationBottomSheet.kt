package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.Shapes

@Composable
fun Explanation(explanationStateFlow: StateFlow<ExplanationState>) {
    val explanationState by explanationStateFlow.collectAsState()
    val explanation: ExplanationState = explanationState

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
        when (explanation) {
            ExplanationState.Calculating -> {
                Text("Calculating...") // TODO(oleg): Add loader
            }
            is ExplanationState.Complete -> {
                ExplanationContent(from = explanation.from, to = (explanationState as ExplanationState.Complete).to)
            }
        }
    }
}

@Composable
@Preview(name = "Explanation Calculating Light", group = "Calculating")
private fun PreviewExplanationCalculating() {
    NumberSystemsTheme {
        Surface {
            Explanation(explanationStateFlow = MutableStateFlow(ExplanationState.Calculating))
        }
    }
}

@Composable
@Preview(name = "Explanation Calculating Dark", group = "Calculating")
private fun PreviewExplanationCalculatingDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            Explanation(explanationStateFlow = MutableStateFlow(ExplanationState.Calculating))
        }
    }
}

@Composable
@Preview(name = "Explanation Complete Light", group = "Complete")
private fun PreviewExplanation() {
    NumberSystemsTheme {
        Surface {
            Explanation(explanationStateFlow = MutableStateFlow(ExplanationState.Complete(from = NumberSystem(value = "10.5", Radix(10)), to = NumberSystem("1010.1", Radix(2)))))
        }
    }
}

@Composable
@Preview(name = "Explanation Complete Dark", group = "Complete")
private fun PreviewExplanationDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            Explanation(explanationStateFlow = MutableStateFlow(ExplanationState.Complete(from = NumberSystem(value = "10.10", Radix(10)), to = NumberSystem("1010.1", Radix(2)))))
        }
    }
}
