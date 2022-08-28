package ru.sandello.binaryconverter.ui.explanation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Composable
fun ExplanationScreen(explanationStateFlow: StateFlow<ExplanationState>) {
    val explanationState by explanationStateFlow.collectAsState()
    val explanation: ExplanationState = explanationState

    Column {
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
            ExplanationScreen(explanationStateFlow = MutableStateFlow(ExplanationState.Calculating))
        }
    }
}

@Composable
@Preview(name = "Explanation Calculating Dark", group = "Calculating")
private fun PreviewExplanationCalculatingDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(explanationStateFlow = MutableStateFlow(ExplanationState.Calculating))
        }
    }
}

@Composable
@Preview(name = "Explanation Complete Light", group = "Complete")
private fun PreviewExplanation() {
    NumberSystemsTheme {
        Surface {
            ExplanationScreen(explanationStateFlow = MutableStateFlow(ExplanationState.Complete(from = NumberSystem(value = "10.5", Radix(10)), to = NumberSystem("1010.1", Radix(2)))))
        }
    }
}

@Composable
@Preview(name = "Explanation Complete Dark", group = "Complete")
private fun PreviewExplanationDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ExplanationScreen(explanationStateFlow = MutableStateFlow(ExplanationState.Complete(from = NumberSystem(value = "10.10", Radix(10)), to = NumberSystem("1010.1", Radix(2)))))
        }
    }
}
