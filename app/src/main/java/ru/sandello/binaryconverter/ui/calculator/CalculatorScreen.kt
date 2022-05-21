package ru.sandello.binaryconverter.ui.calculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import ru.sandello.binaryconverter.model.NumberSystem
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
                    value = viewModel.numberSystemCustom1.value.value,
                    onValueChange = { textFieldValue ->
                        viewModel.convertFrom(
                            operandType = OperandType.OperandCustom1,
                            from = NumberSystem(value = textFieldValue, radix = viewModel.numberSystemCustom1.value.radix),
                        )
                    },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.numberSystemCustom1.value.radix.value)) },
                    isError = viewModel.numberSystem1error.value,
                    visualTransformation = OperandVisualTransformation(viewModel.numberSystemCustom1.value.radix),
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
                        value = viewModel.numberSystemCustom1.value.radix.value.toString(),
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
                                    viewModel.updateRadix(radixType = RadixType.RadixCustom1, newRadix = radix)
                                    expanded = false
                                }
                            ) {
                                Text(text = radix.value.toString())
                            }
                        }
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                viewModel.arithmeticOptions.forEach { arithmetic ->
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
                                Multiply -> "×"
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
                    value = viewModel.numberSystemCustom2.value.value,
                    onValueChange = { textFieldValue ->
                        viewModel.convertFrom(
                            operandType = OperandType.OperandCustom2,
                            from = NumberSystem(value = textFieldValue, radix = viewModel.numberSystemCustom2.value.radix),
                        )
                    },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.numberSystemCustom2.value.radix.value)) },
                    isError = viewModel.numberSystem2error.value,
                    visualTransformation = OperandVisualTransformation(viewModel.numberSystemCustom2.value.radix),
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
                        value = viewModel.numberSystemCustom2.value.radix.value.toString(),
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
                                    viewModel.updateRadix(radixType = RadixType.RadixCustom2, newRadix = radix)
                                    expanded = false
                                }
                            ) {
                                Text(text = radix.value.toString())
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
                    value = viewModel.numberSystemResult.value.value,
                    onValueChange = { },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                            width = Dimension.fillToConstraints
                        },
                    readOnly = true,
                    label = { Text(stringResource(R.string.radix, viewModel.numberSystemResult.value.radix.value)) },
                    visualTransformation = OperandVisualTransformation(viewModel.numberSystemResult.value.radix),
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
                        value = viewModel.numberSystemResult.value.radix.value.toString(),
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
                                    viewModel.updateRadix(radixType = RadixType.RadixResult, newRadix = radix)
                                    expanded = false
                                }
                            ) {
                                Text(text = radix.value.toString())
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

@Preview
@Composable
fun PreviewCalculatorScreenDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            CalculatorScreen(viewModel = CalculatorViewModel(), mainPadding = PaddingValues())
        }
    }
}
