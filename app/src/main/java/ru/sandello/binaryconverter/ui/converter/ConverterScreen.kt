package ru.sandello.binaryconverter.ui.converter

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
import androidx.hilt.navigation.compose.hiltViewModel
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel = hiltViewModel(), mainPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.imePadding(),
        contentPadding = PaddingValues(
            start = WindowInsets.displayCutout.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current) + 8.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp,
            end = WindowInsets.displayCutout.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current) + 8.dp,
            bottom = maxOf(mainPadding.calculateBottomPadding() + 64.dp, 72.dp) + 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            OutlinedTextField(
                value = viewModel.numberSystem10.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem10.value.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem10.value.radix),
                isError = viewModel.numberSystem10error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem2.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem2.value.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.bin)) },
                isError = viewModel.numberSystem2error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem2.value.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem8.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, viewModel.numberSystem8.value.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.oct)) },
                isError = viewModel.numberSystem8error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem8.value.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem16.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem16.value.radix)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.hex)) },
                isError = viewModel.numberSystem16error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem16.value.radix),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.numberSystemCustom.value.value,
                    onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystemCustom.value.radix)) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.radix, viewModel.numberSystemCustom.value.radix.value)) },
                    isError = viewModel.numberSystemCustomError.value,
                    visualTransformation = OperandVisualTransformation(viewModel.numberSystemCustom.value.radix),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = MaterialTheme.shapes.medium,
                )
                var expanded by remember { mutableStateOf(false) }
                RadixExposedDropdown(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    onRadixClicked = { radix ->
                        viewModel.updateCustomRadix(radix)
                        expanded = false
                    },
                    radix = viewModel.numberSystemCustom.value.radix,
                    radixes = viewModel.radixes,
                    modifier = Modifier.width(96.dp),
                    isCompact = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConverterScreen() {
    NumberSystemsTheme {
        Surface {
            ConverterScreen(mainPadding = PaddingValues())
        }
    }
}


@Preview
@Composable
private fun PreviewConverterScreenDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            ConverterScreen(mainPadding = PaddingValues())
        }
    }
}
