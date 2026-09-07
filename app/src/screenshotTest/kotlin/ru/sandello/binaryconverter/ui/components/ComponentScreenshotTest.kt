package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.PhoneLightDarkPreviews
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun NumberSystemFieldFilledScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            NumberSystemField(
                value = "1234567",
                radix = Radix.DEC,
                onValueChange = {},
            )
        }
    }
}

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun NumberSystemFieldErrorScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            NumberSystemField(
                value = "2",
                radix = Radix.BIN,
                isError = true,
                onValueChange = {},
            )
        }
    }
}

@PreviewTest
@PhoneLightDarkPreviews
@Composable
fun FabStackScreenshot() {
    NumberSystemsTheme(dynamicColor = false) {
        Surface {
            FabStack(
                isClearFabVisible = true,
                isExplanationFabVisible = true,
                onClearClicked = {},
                onExplanationClicked = {},
            )
        }
    }
}
