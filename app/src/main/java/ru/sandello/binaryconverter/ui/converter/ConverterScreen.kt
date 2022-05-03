package ru.sandello.binaryconverter.ui.converter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.OperandVisualTransformation

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
                value = viewModel.operand10.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 10, fromValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = OperandVisualTransformation(10),
                isError = viewModel.operand10error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand2.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 2, fromValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.bin)) },
                isError = viewModel.operand2error.value,
                visualTransformation = OperandVisualTransformation(2),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand8.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 8, fromValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.oct)) },
                isError = viewModel.operand8error.value,
                visualTransformation = OperandVisualTransformation(8),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand16.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 16, fromValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.hex)) },
                isError = viewModel.operand16error.value,
                visualTransformation = OperandVisualTransformation(16),
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
                    value = viewModel.operandCustom.value,
                    onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = viewModel.customRadix.value, fromValue = textFieldValue) },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.customRadix.value)) },
                    isError = viewModel.operandCustomError.value,
                    visualTransformation = OperandVisualTransformation(viewModel.customRadix.value),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(16.dp),
                )
                var expanded by remember { mutableStateOf(false) }
                @OptIn(ExperimentalMaterialApi::class)
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
                        value = viewModel.customRadix.value.toString(),
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
                        viewModel.customRadixes.forEach { radix ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.updateCustomRadix(radix)
                                    expanded = false
                                }
                            ) {
                                Text(text = radix.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}
