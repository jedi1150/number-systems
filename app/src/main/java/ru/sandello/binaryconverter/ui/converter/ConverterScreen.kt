package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.COMMA
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ConverterRoute(
    viewModel: ConverterViewModel = hiltViewModel(),
) {
    val converterUiState by viewModel.converterUiState.collectAsStateWithLifecycle()

    ConverterScreen(
        converterUiState = converterUiState,
        onNumberSystemChanged = viewModel::convertFrom,
        onCustomRadixChanged = viewModel::updateCustomRadix,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    converterUiState: ConverterUiState,
    onNumberSystemChanged: (NumberSystem) -> Unit,
    onCustomRadixChanged: (Radix) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.imePadding(),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 72.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem10.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem10.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = OperandVisualTransformation(converterUiState.numberSystem10.radix),
                isError = converterUiState.numberSystem10Error,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem2.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem2.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.bin)) },
                isError = converterUiState.numberSystem2Error,
                visualTransformation = OperandVisualTransformation(converterUiState.numberSystem2.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem8.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), converterUiState.numberSystem8.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.oct)) },
                isError = converterUiState.numberSystem8Error,
                visualTransformation = OperandVisualTransformation(converterUiState.numberSystem8.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem16.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem16.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.hex)) },
                isError = converterUiState.numberSystem16Error,
                visualTransformation = OperandVisualTransformation(converterUiState.numberSystem16.radix),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = converterUiState.numberSystemCustom.value,
                    onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystemCustom.radix)) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.radix, converterUiState.numberSystemCustom.radix.value)) },
                    isError = converterUiState.numberSystemCustomError,
                    visualTransformation = OperandVisualTransformation(converterUiState.numberSystemCustom.radix),
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
                    radix = converterUiState.numberSystemCustom.radix,
                    radixes = converterUiState.radixes,
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
                converterUiState = ConverterUiState(),
                onNumberSystemChanged = {},
            ) {}
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
                converterUiState = ConverterUiState(),
                onNumberSystemChanged = {},
            ) {}
        }
    }
}
