package ru.sandello.binaryconverter.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.DigitGroupingVisualTransformation
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.COMMA
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun NumberSystemField(
    value: String,
    radix: Radix,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isGroupingEnabled: Boolean = true,
    onValueChange: (NumberSystem) -> Unit,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }

    var lastTextValue by remember(value) { mutableStateOf(value) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newTextFieldValueState ->
            textFieldValueState = newTextFieldValueState

            val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
            lastTextValue = newTextFieldValueState.text

            if (stringChangedSinceLastInvocation) {
                onValueChange(NumberSystem(value = newTextFieldValueState.text.replace(COMMA, NS_DELIMITER), radix = radix))
            }
        },
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(fontFamily = RobotoMonoFamily),
        label = { Text(radix.value.toString()) },
        visualTransformation = if (isGroupingEnabled) DigitGroupingVisualTransformation(radix) else VisualTransformation.None,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        shape = MaterialTheme.shapes.medium,
    )
}

@Preview
@Composable
private fun NumberSystemFieldPreview() {
    var value by remember {
        mutableStateOf("")
    }

    NumberSystemsTheme {
        Surface {
            NumberSystemField(
                value = value,
                radix = Radix.DEC,
                onValueChange = { value = it.value },
            )
        }
    }
}
