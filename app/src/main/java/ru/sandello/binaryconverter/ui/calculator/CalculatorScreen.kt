package ru.sandello.binaryconverter.ui.calculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel, mainPadding: PaddingValues) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val options = listOf("3", "4", "5", "6", "7")
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var expanded by remember { mutableStateOf(false) }
    val actionOptions = listOf("+", "-", "*", "/")
    var selectedOption by remember { mutableStateOf(0) }

    LazyColumn(
        contentPadding = PaddingValues(
            start = 8.dp,
            top = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = true,
                applyBottom = true,
            ).calculateTopPadding(),
            end = 8.dp,
            bottom = mainPadding.calculateBottomPadding() + 72.dp,
        ),
    ) {
        item {
            ConstraintLayout(modifier = Modifier.padding(vertical = 4.dp)) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(exposedDropdown.start, margin = 4.dp)
                    },
                    label = { Text(stringResource(R.string.base_value)) },
                    shape = RoundedCornerShape(16.dp),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MaterialTheme.colors.primary, unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,)
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.constrainAs(exposedDropdown) {
                        start.linkTo(textField.end, margin = 4.dp)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent
                    },
                ) {
                    OutlinedTextField(
                        value = selectedOptionText,
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
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption)
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
                horizontalArrangement = Arrangement.Center
            ) {
                itemsIndexed(actionOptions) { index, option ->
                    val tint by animateColorAsState(
                        if (selectedOption == index) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                    OutlinedButton(
                        onClick = { selectedOption = index },
//                        colors = ButtonDefaults.outlinedButtonColors(containerColor = tint),
                    ) {
                        Text(option)
                    }
                }
            }
        }
        item {
            ConstraintLayout(modifier = Modifier.padding(vertical = 4.dp)) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(stringResource(R.string.base_value)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(exposedDropdown.start, margin = 4.dp)
                    }
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.constrainAs(exposedDropdown) {
                        start.linkTo(textField.end, margin = 4.dp)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent
                    },
                ) {
                    OutlinedTextField(
                        value = selectedOptionText,
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
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption)
                            }
                        }
                    }
                }
            }
        }
        item {
            ConstraintLayout(modifier = Modifier.padding(vertical = 4.dp)) {
                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(stringResource(R.string.base_value)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(exposedDropdown.start, margin = 4.dp)
                    }
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.constrainAs(exposedDropdown) {
                        start.linkTo(textField.end, margin = 4.dp)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent
                    },
                ) {
                    OutlinedTextField(
                        value = selectedOptionText,
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
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption)
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
