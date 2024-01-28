package ru.sandello.binaryconverter.ui.calculator

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.OperandVisualTransformation
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Addition
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Divide
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Multiply
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Subtraction
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.COMMA
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun CalculatorRoute(
    viewModel: CalculatorViewModel = hiltViewModel(),
) {
    val calculatorUiState by viewModel.calculatorUiState.collectAsStateWithLifecycle()

    CalculatorScreen(
        calculatorUiState = calculatorUiState,
        onNumberSystemChange = viewModel::convertFrom,
        onRadixChange = viewModel::updateRadix,
        onArithmeticChange = viewModel::selectArithmetic,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    calculatorUiState: CalculatorUiState,
    onNumberSystemChange: (CalculatorOperandType, NumberSystem) -> Unit,
    onRadixChange: (CalculatorRadixType, Radix) -> Unit,
    onArithmeticChange: (ArithmeticType) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 72.dp,
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
                            NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = calculatorUiState.numberSystemCustom1.radix),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontFamily = RobotoMonoFamily),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                calculatorUiState.arithmeticTypes.forEach { arithmetic ->
                    val checked = calculatorUiState.selectedArithmetic == arithmetic
                    val border by animateDpAsState(if (checked) TextFieldDefaults.FocusedIndicatorThickness else TextFieldDefaults.UnfocusedIndicatorThickness, label = "borderWidth")
                    val borderColor by animateColorAsState(if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, label = "borderColor")
                    OutlinedIconToggleButton(
                        checked = checked,
                        onCheckedChange = { if (it) onArithmeticChange(arithmetic) },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(border, color = borderColor),
                        colors = IconButtonDefaults.outlinedIconToggleButtonColors(checkedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = when (arithmetic) {
                                Addition -> "+"
                                Subtraction -> "-"
                                Multiply -> "×"
                                Divide -> "÷"
                            },
                            fontFamily = RobotoMonoFamily,
                            style = MaterialTheme.typography.bodyLarge,
                        )
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
                            NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = calculatorUiState.numberSystemCustom2.radix),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontFamily = RobotoMonoFamily),
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
                    textStyle = TextStyle(fontFamily = RobotoMonoFamily),
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

@Preview(device = "spec:width=411dp,height=891dp", wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:width=411dp,height=891dp", wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewCalculatorScreen() {
    NumberSystemsTheme {
        Surface {
            CalculatorScreen(
                calculatorUiState = CalculatorUiState(),
                onNumberSystemChange = { _, _ -> },
                onRadixChange = { _, _ -> },
            ) {}
        }
    }
}
