package ru.sandello.binaryconverter.ui.converter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsPadding
import ru.sandello.binaryconverter.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ConverterScreen() {
    val viewModel: ConverterViewModel by viewModel()

    val textState = remember { mutableStateOf(TextFieldValue()) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        item {
            Spacer(modifier = Modifier.statusBarsPadding())
        }
        item {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.dec)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.bin)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text(stringResource(R.string.oct)) },
                shape = RoundedCornerShape(16.dp),
            )
        }
        item {
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
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
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    label = { Text(stringResource(R.string.base_value)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(exposedDropdown.start, margin = 4.dp)
                    }
                )
                val options = listOf("3", "4", "5", "6", "7")
                var selectedOptionText by remember { mutableStateOf(options[0]) }
                var expanded by remember { mutableStateOf(false) }
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
