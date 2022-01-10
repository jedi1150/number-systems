package ru.sandello.binaryconverter.ui.converter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import ru.sandello.binaryconverter.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel, mainPadding: PaddingValues) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 8.dp,
            top = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyTop = true,
                applyBottom = true,
            ).calculateTopPadding(),
            end = 8.dp,
            bottom = mainPadding.calculateBottomPadding() + 88.dp,
        ),
    ) {
        item {
            OutlinedTextField(
                value = viewModel.operand10.value,
                onValueChange = { textFieldValue -> viewModel.convert(fromRadix = 10, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.dec)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand2.value,
                onValueChange = { textFieldValue -> viewModel.convert(fromRadix = 2, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.bin)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand8.value,
                onValueChange = { textFieldValue -> viewModel.convert(fromRadix = 8, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.oct)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand16.value,
                onValueChange = { textFieldValue -> viewModel.convert(fromRadix = 16, textFieldValue = textFieldValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.hex)) },
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
                    onValueChange = { textFieldValue -> viewModel.convert(fromRadix = viewModel.customRadix.value, textFieldValue = textFieldValue) },
                    label = { Text(stringResource(R.string.radix, viewModel.customRadix.value)) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, autoCorrect = false, keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(exposedDropdown.start, margin = 4.dp)
                    }
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
                        viewModel.customRadixes.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.updateCustomRadix(selectionOption)
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption.toString())
                            }
                        }
                    }
                }
            }
        }
    }
//}
}
