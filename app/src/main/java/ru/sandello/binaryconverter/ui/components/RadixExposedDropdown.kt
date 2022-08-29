@file:OptIn(ExperimentalMaterial3Api::class)

package ru.sandello.binaryconverter.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import ru.sandello.binaryconverter.model.Radix
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
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = TextFieldDefaults.outlinedShape,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
    ) {
        RadixOutlinedTextField(
            value = radix.value.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = trailingIcon,
            shape = shape,
            singleLine = true,
        )
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
private fun PreviewRadixExposedDropdown() {
    NumberSystemsTheme {
        Surface {
            RadixExposedDropdown(
                expanded = false,
                onExpandedChange = {},
                onDismissRequest = {},
                onRadixClicked = {},
                radix = Radix(16),
                radixes = mutableListOf(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRadixExposedDropdownDark() {
    NumberSystemsTheme(darkTheme = true) {
        Surface {
            RadixExposedDropdown(
                expanded = false,
                onExpandedChange = {},
                onDismissRequest = {},
                onRadixClicked = {},
                radix = Radix(16),
                radixes = mutableListOf(),
            )
        }
    }
}
