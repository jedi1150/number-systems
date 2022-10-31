package ru.sandello.binaryconverter.ui.calculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.TextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.*
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    calculatorUiState: CalculatorUiState,
    mainPadding: PaddingValues,
    onNumberSystemChange: (CalculatorOperandType, NumberSystem) -> Unit,
    onRadixChange: (CalculatorRadixType, Radix) -> Unit,
    onArithmeticChange: (ArithmeticType) -> Unit,
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = calculatorUiState.numberSystemCustom1.value,
                    onValueChange = { textFieldValue ->
                        onNumberSystemChange(
                            CalculatorOperandType.OperandCustom1,
                            NumberSystem(value = textFieldValue, radix = calculatorUiState.numberSystemCustom1.radix),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.radix, calculatorUiState.numberSystemCustom1.radix.value)) },
                    isError = calculatorUiState.numberSystemCustom1Error,
                    visualTransformation = OperandVisualTransformation(calculatorUiState.numberSystemCustom1.radix),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                var expanded by remember { mutableStateOf(false) }
                RadixExposedDropdown(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    onRadixClicked = { radix ->
                        onRadixChange(CalculatorRadixType.RadixCustom1, radix)
                        expanded = false
                    },
                    radix = calculatorUiState.numberSystemCustom1.radix,
                    radixes = calculatorUiState.radixes,
                    modifier = Modifier.width(96.dp),
                    isCompact = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    shape = MaterialTheme.shapes.medium,
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                calculatorUiState.arithmeticTypes.forEach { arithmetic ->
                    val checked = calculatorUiState.selectedArithmetic == arithmetic
                    val border by animateDpAsState(if (checked) FocusedBorderThickness else UnfocusedBorderThickness)
                    val borderColor by animateColorAsState(if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                    OutlinedIconToggleButton(checked = checked,
                        onCheckedChange = { if (it) onArithmeticChange(arithmetic) },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(border, color = borderColor),
                        colors = IconButtonDefaults.outlinedIconToggleButtonColors(checkedContainerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Text(text = when (arithmetic) {
                            Addition -> "+"
                            Subtraction -> "−"
                            Multiply -> "×"
                            Divide -> "÷"
                        })
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = calculatorUiState.numberSystemCustom2.value,
                    onValueChange = { textFieldValue ->
                        onNumberSystemChange(
                            CalculatorOperandType.OperandCustom2,
                            NumberSystem(value = textFieldValue, radix = calculatorUiState.numberSystemCustom2.radix),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.radix, calculatorUiState.numberSystemCustom2.radix.value)) },
                    isError = calculatorUiState.numberSystemCustom2Error,
                    visualTransformation = OperandVisualTransformation(calculatorUiState.numberSystemCustom2.radix),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done,
                    ),
                    shape = MaterialTheme.shapes.medium,
                )
                var expanded by remember { mutableStateOf(false) }
                RadixExposedDropdown(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    onRadixClicked = { radix ->
                        onRadixChange(CalculatorRadixType.RadixCustom2, radix)
                        expanded = false
                    },
                    radix = calculatorUiState.numberSystemCustom2.radix,
                    radixes = calculatorUiState.radixes,
                    modifier = Modifier.width(96.dp),
                    isCompact = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    shape = MaterialTheme.shapes.medium,
                )
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = calculatorUiState.numberSystemResult.value,
                    onValueChange = { },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    label = { Text(stringResource(R.string.radix, calculatorUiState.numberSystemResult.radix.value)) },
                    visualTransformation = OperandVisualTransformation(calculatorUiState.numberSystemResult.radix),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = MaterialTheme.shapes.medium,
                )
                var expanded by remember { mutableStateOf(false) }
                RadixExposedDropdown(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false },
                    onRadixClicked = { radix ->
                        onRadixChange(CalculatorRadixType.RadixResult, radix)
                        expanded = false
                    },
                    radix = calculatorUiState.numberSystemResult.radix,
                    radixes = calculatorUiState.radixes,
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

@Composable
@Preview
fun PreviewCalculatorScreen() {
    NumberSystemsTheme {
        Surface {
            CalculatorScreen(
                calculatorUiState = CalculatorUiState(),
                mainPadding = PaddingValues(),
                onNumberSystemChange = { _, _ -> },
                onRadixChange = { _, _ -> },
                onArithmeticChange = {},
            )
        }
    }
}

@Preview
@Composable
fun PreviewCalculatorScreenDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            CalculatorScreen(
                calculatorUiState = CalculatorUiState(),
                mainPadding = PaddingValues(),
                onNumberSystemChange = { _, _ -> },
                onRadixChange = { _, _ -> },
                onArithmeticChange = {},
            )
        }
    }
}
