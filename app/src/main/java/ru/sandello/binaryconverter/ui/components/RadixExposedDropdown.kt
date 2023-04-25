@file:OptIn(ExperimentalMaterial3Api::class)

package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun RadixExposedDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onRadixClicked: (Radix) -> Unit,
    radix: Radix,
    radixes: List<Radix>,
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = TextFieldDefaults.outlinedShape,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        if (isCompact) {
            OutlinedTextField(
                value = radix.value.toString(),
                onValueChange = { },
                modifier = Modifier.menuAnchor(),
                enabled = false,
                readOnly = true,
                trailingIcon = trailingIcon,
                singleLine = true,
                shape = shape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        } else {
            OutlinedTextField(
                value = radix.value.toString(),
                onValueChange = {},
                modifier = Modifier.menuAnchor(),
                enabled = false,
                readOnly = true,
                label = {},
                trailingIcon = trailingIcon,
                singleLine = true,
                shape = shape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            radixes.forEach { radix ->
                DropdownMenuItem(
                    text = {
                        Text(text = radix.value.toString())
                    },
                    onClick = { onRadixClicked(radix) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRadixExposedDropdownCompact() {
    var expanded by remember { mutableStateOf(false) }

    NumberSystemsTheme {
        Surface {
            RadixExposedDropdown(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                onDismissRequest = { expanded = false },
                onRadixClicked = { expanded = false },
                radix = Radix.HEX,
                radixes = listOf(Radix.BIN, Radix.OCT, Radix.DEC, Radix.HEX),
                isCompact = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRadixExposedDropdownDark() {
    var expanded by remember { mutableStateOf(false) }

    NumberSystemsTheme(darkTheme = true) {
        Surface {
            RadixExposedDropdown(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                onDismissRequest = { expanded = false },
                onRadixClicked = { expanded = false },
                radix = Radix.HEX,
                radixes = listOf(Radix.BIN, Radix.OCT, Radix.DEC, Radix.HEX),
                isCompact = false,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
            )
        }
    }
}
