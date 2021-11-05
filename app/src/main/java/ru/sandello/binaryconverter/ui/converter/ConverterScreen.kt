package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.utils.APP_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(viewModel: ConverterViewModel, mainPadding: PaddingValues) {
    Log.d(APP_TAG, "ConverterScreen:: init")
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
            OutlinedTextField(
                value = viewModel.operand10new.value,
                onValueChange = { viewModel.updateOperand(fraction = 10, textFieldValue = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.dec)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand2new.value,
                onValueChange = { viewModel.updateOperand(fraction = 2, textFieldValue = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.bin)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand8new.value,
                onValueChange = { viewModel.updateOperand(fraction = 8, textFieldValue = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.oct)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = viewModel.operand16new.value,
                onValueChange = { viewModel.updateOperand(fraction = 16, textFieldValue = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.hex)) },
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
                    value = viewModel.operandCustomNew.value,
                    onValueChange = { viewModel.updateOperand(fraction = viewModel.customBaseNumber.value, textFieldValue = it) },
                    label = { Text(stringResource(R.string.base_value, viewModel.customBaseNumber.value)) },
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
                        value = viewModel.customBaseNumber.value.toString(),
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
                        viewModel.customBaseNumbers.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.updateCustomBaseNumber(selectionOption)
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
