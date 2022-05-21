package ru.sandello.binaryconverter.ui.converter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun ConverterScreen(viewModel: ConverterViewModel, mainPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.imePadding(),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp,
            end = 8.dp,
            bottom = maxOf(mainPadding.calculateBottomPadding() + 64.dp, 72.dp) + 8.dp,
        ),
    ) {
        item {
            OutlinedTextField(
                value = viewModel.numberSystem10.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem10.value.radix)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem10.value.radix),
                isError = viewModel.numberSystem10error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem2.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem2.value.radix)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.bin)) },
                isError = viewModel.numberSystem2error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem2.value.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem8.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, viewModel.numberSystem8.value.radix)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.oct)) },
                isError = viewModel.numberSystem8error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem8.value.radix),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.numberSystem16.value.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystem16.value.radix)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.hex)) },
                isError = viewModel.numberSystem16error.value,
                visualTransformation = OperandVisualTransformation(viewModel.numberSystem16.value.radix),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = viewModel.numberSystemCustom.value.value,
                    onValueChange = { textFieldValue -> viewModel.convertFrom(NumberSystem(value = textFieldValue, radix = viewModel.numberSystemCustom.value.radix)) },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.numberSystemCustom.value.radix.value)) },
                    isError = viewModel.numberSystemCustomError.value,
                    visualTransformation = OperandVisualTransformation(viewModel.numberSystemCustom.value.radix),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(16.dp),
                )
                var expanded by remember { mutableStateOf(false) }
                @OptIn(ExperimentalMaterial3Api::class)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.constrainAs(exposedDropdown) {
                        start.linkTo(textField.end, margin = 4.dp)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent.atMost(120.dp)
                    },
                ) {
                    OutlinedTextField(
                        value = viewModel.numberSystemCustom.value.radix.value.toString(),
                        onValueChange = { },
                        readOnly = true,
                        label = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                    ) {
                        viewModel.radixes.forEach { radix ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = radix.value.toString())
                                },
                                onClick = {
                                    viewModel.updateCustomRadix(radix)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConverterScreen() {
    NumberSystemsTheme {
        Surface {
            ConverterScreen(viewModel = ConverterViewModel(), mainPadding = PaddingValues())
        }
    }
}


@Preview
@Composable
private fun PreviewConverterScreenDark() {
    NumberSystemsTheme(isDarkTheme = true) {
        Surface {
            ConverterScreen(viewModel = ConverterViewModel(), mainPadding = PaddingValues())
        }
    }
}
