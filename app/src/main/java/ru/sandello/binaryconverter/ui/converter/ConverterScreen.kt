package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    radixes: List<Radix>,
    numberSystem10: NumberSystem,
    numberSystem2: NumberSystem,
    numberSystem8: NumberSystem,
    numberSystem16: NumberSystem,
    numberSystemCustom: NumberSystem,
    numberSystem10error: Boolean,
    numberSystem2error: Boolean,
    numberSystem8error: Boolean,
    numberSystem16error: Boolean,
    numberSystemCustomError: Boolean,
    mainPadding: PaddingValues,
    onNumberSystemChange: (NumberSystem) -> Unit,
    onCustomRadixChanged: (Radix) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    LazyColumn(
        modifier = Modifier.imePadding(),
        contentPadding = PaddingValues(
            start = WindowInsets.navigationBars.asPaddingValues().calculateStartPadding(layoutDirection) + WindowInsets.displayCutout.asPaddingValues().calculateStartPadding(layoutDirection) + 8.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp,
            end = WindowInsets.navigationBars.asPaddingValues().calculateEndPadding(layoutDirection) + WindowInsets.displayCutout.asPaddingValues().calculateEndPadding(layoutDirection) + 8.dp,
            bottom = maxOf(mainPadding.calculateBottomPadding() + 64.dp, 72.dp) + 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            OutlinedTextField(
                value = numberSystem10.value,
                onValueChange = { textFieldValue -> onNumberSystemChange(NumberSystem(value = textFieldValue, radix = numberSystem10.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = OperandVisualTransformation(numberSystem10.radix),
                isError = numberSystem10error,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = numberSystem2.value,
                onValueChange = { textFieldValue -> onNumberSystemChange(NumberSystem(value = textFieldValue, radix = numberSystem2.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.bin)) },
                isError = numberSystem2error,
                visualTransformation = OperandVisualTransformation(numberSystem2.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = numberSystem8.value,
                onValueChange = { textFieldValue -> onNumberSystemChange(NumberSystem(value = textFieldValue, numberSystem8.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.oct)) },
                isError = numberSystem8error,
                visualTransformation = OperandVisualTransformation(numberSystem8.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = numberSystem16.value,
                onValueChange = { textFieldValue -> onNumberSystemChange(NumberSystem(value = textFieldValue, radix = numberSystem16.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.hex)) },
                isError = numberSystem16error,
                visualTransformation = OperandVisualTransformation(numberSystem16.radix),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = numberSystemCustom.value,
                    onValueChange = { textFieldValue -> onNumberSystemChange(NumberSystem(value = textFieldValue, radix = numberSystemCustom.radix)) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.radix, numberSystemCustom.radix.value)) },
                    isError = numberSystemCustomError,
                    visualTransformation = OperandVisualTransformation(numberSystemCustom.radix),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = MaterialTheme.shapes.medium,
                )
                var expanded by remember { mutableStateOf(false) }
                RadixExposedDropdown(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    onRadixClicked = { radix ->
                        onCustomRadixChanged(radix)
                        expanded = false
                    },
                    radix = numberSystemCustom.radix,
                    radixes = radixes,
                    modifier = Modifier.width(96.dp),
                    isCompact = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    shape = MaterialTheme.shapes.medium,
                )
            }
        }
    }
}

@SuppressLint("Range")
@Preview
@Composable
private fun PreviewConverterScreen() {
    NumberSystemsTheme {
        Surface {
            ConverterScreen(
                radixes = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) },
                numberSystem10 = NumberSystem(String(), Radix.DEC),
                numberSystem2 = NumberSystem(String(), Radix.BIN),
                numberSystem8 = NumberSystem(String(), Radix.OCT),
                numberSystem16 = NumberSystem(String(), Radix.HEX),
                numberSystemCustom = NumberSystem(String(), Radix(3)),
                numberSystem10error = false,
                numberSystem2error = false,
                numberSystem8error = false,
                numberSystem16error = false,
                numberSystemCustomError = false,
                mainPadding = PaddingValues(),
                onNumberSystemChange = {},
                onCustomRadixChanged = {},
            )
        }
    }
}


@SuppressLint("Range")
@Preview
@Composable
private fun PreviewConverterScreenDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ConverterScreen(
                radixes = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) },
                numberSystem10 = NumberSystem(String(), Radix.DEC),
                numberSystem2 = NumberSystem(String(), Radix.BIN),
                numberSystem8 = NumberSystem(String(), Radix.OCT),
                numberSystem16 = NumberSystem(String(), Radix.HEX),
                numberSystemCustom = NumberSystem(String(), Radix(3)),
                numberSystem10error = false,
                numberSystem2error = false,
                numberSystem8error = false,
                numberSystem16error = false,
                numberSystemCustomError = false,
                mainPadding = PaddingValues(),
                onNumberSystemChange = {},
                onCustomRadixChanged = {},
            )
        }
    }
}
