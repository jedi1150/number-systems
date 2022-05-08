package ru.sandello.binaryconverter.ui.calculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.FocusedBorderThickness
import androidx.compose.material.TextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.*
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel, mainPadding: PaddingValues) {

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
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = viewModel.operandCustom1.value,
                    onValueChange = { textFieldValue ->
                        viewModel.convertFrom(
                            operandType = OperandType.OperandCustom1,
                            fromRadix = viewModel.radixCustom1.value,
                            fromValue = textFieldValue,
                        )
                    },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.radixCustom1.value)) },
                    isError = viewModel.operandCustom1error.value,
                    visualTransformation = OperandVisualTransformation(viewModel.radixCustom1.value),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
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
                        value = viewModel.radixCustom1.value.toString(),
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
                                onClick = {
                                    viewModel.updateRadix(radixType = RadixType.RadixCustom1, radix = radix)
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
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                itemsIndexed(viewModel.arithmeticOptions) { index, arithmetic ->
                    val checked = viewModel.selectedArithmetic.value == arithmetic
                    val tint by animateColorAsState(if (checked) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled))
                    val border by animateDpAsState(if (checked) FocusedBorderThickness else UnfocusedBorderThickness)
                    IconToggleButton(
                        checked = checked,
                        onCheckedChange = { if (it) viewModel.selectArithmetic(arithmetic) },
                        interactionSource = remember { MutableInteractionSource() },
                        modifier = Modifier.border(
                            BorderStroke(border, color = tint),
                            RoundedCornerShape(16.dp),
                        ),
                    ) {
                        Text(
                            text = when (arithmetic) {
                                Addition -> "+"
                                Subtraction -> "-"
                                Multiply -> "*"
                                Divide -> "/"
                            }
                        )
                    }
                }
            }
        }
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = viewModel.operandCustom2.value,
                    onValueChange = { textFieldValue ->
                        viewModel.convertFrom(
                            operandType = OperandType.OperandCustom2,
                            fromRadix = viewModel.radixCustom2.value,
                            fromValue = textFieldValue,
                        )
                    },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.radixCustom2.value)) },
                    isError = viewModel.operandCustom2error.value,
                    visualTransformation = OperandVisualTransformation(viewModel.radixCustom2.value),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
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
                        value = viewModel.radixCustom2.value.toString(),
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
                                onClick = {
                                    viewModel.updateRadix(radixType = RadixType.RadixCustom2, radix = radix)
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
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = viewModel.operandResult.value,
                    onValueChange = { },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.radixResult.value)) },
                    visualTransformation = OperandVisualTransformation(viewModel.radixResult.value),
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
                        value = viewModel.radixResult.value.toString(),
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
                                onClick = {
                                    viewModel.updateRadix(radixType = RadixType.RadixResult, radix = radix)
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

@Composable
@Preview
fun PreviewCalculatorScreen() {
    NumberSystemsTheme {
        Surface {
            CalculatorScreen(viewModel = CalculatorViewModel(), mainPadding = PaddingValues())
        }
    }
}
