package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily

@OptIn(ExperimentalMaterial3Api::class)
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
    shape: Shape = OutlinedTextFieldDefaults.shape,
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
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                trailingIcon = trailingIcon,
                singleLine = true,
                shape = shape,
                colors = OutlinedTextFieldDefaults.colors(
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
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                label = {},
                trailingIcon = trailingIcon,
                singleLine = true,
                shape = shape,
                colors = OutlinedTextFieldDefaults.colors(
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
                        Text(text = radix.value.toString(), fontFamily = RobotoMonoFamily)
                    },
                    onClick = { onRadixClicked(radix) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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
