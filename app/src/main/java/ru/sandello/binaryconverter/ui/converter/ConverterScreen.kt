package ru.sandello.binaryconverter.ui.converter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.R

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.insets.ExperimentalAnimatedInsets::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel, mainPadding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.padding(mainPadding),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = true,
            ).calculateTopPadding(),
            end = 8.dp,
            bottom = 88.dp,
        ),
    ) {
        item {
            val bringIntoViewRequester = remember { BringIntoViewRequester() }

            OutlinedTextField(
                value = viewModel.operand10.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 10, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(250)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = { Text(stringResource(R.string.dec)) },
                isError = viewModel.operand10error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            val bringIntoViewRequester = remember { BringIntoViewRequester() }

            OutlinedTextField(
                value = viewModel.operand2.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 2, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(250)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = { Text(stringResource(R.string.bin)) },
                isError = viewModel.operand2error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            val bringIntoViewRequester = remember { BringIntoViewRequester() }

            OutlinedTextField(
                value = viewModel.operand8.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 8, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(250)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = { Text(stringResource(R.string.oct)) },
                isError = viewModel.operand8error.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            val bringIntoViewRequester = remember { BringIntoViewRequester() }

            OutlinedTextField(
                value = viewModel.operand16.value,
                onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = 16, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                delay(250)
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                label = { Text(stringResource(R.string.hex)) },
                isError = viewModel.operand16error.value,
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
                val bringIntoViewRequester = remember { BringIntoViewRequester() }

                val (textField, exposedDropdown) = createRefs()

                OutlinedTextField(
                    value = viewModel.operandCustom.value,
                    onValueChange = { textFieldValue -> viewModel.convertFrom(fromRadix = viewModel.customRadix.value, textFieldValue = textFieldValue) },
                    modifier = Modifier
                        .constrainAs(textField) {
                            start.linkTo(parent.start)
                            end.linkTo(exposedDropdown.start, margin = 4.dp)
                        }
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(250)
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    label = { Text(stringResource(R.string.radix, viewModel.customRadix.value)) },
                    isError = viewModel.operandCustomError.value,
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
                        width = Dimension.preferredWrapContent
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
