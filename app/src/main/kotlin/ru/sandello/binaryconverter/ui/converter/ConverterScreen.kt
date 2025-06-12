package ru.sandello.binaryconverter.ui.converter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.ui.DigitGroupingVisualTransformation
import ru.sandello.binaryconverter.ui.components.RadixExposedDropdown
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.RobotoMonoFamily
import ru.sandello.binaryconverter.utils.COMMA
import ru.sandello.binaryconverter.utils.NS_DELIMITER

@Composable
fun ConverterRoute(
    contentPadding: PaddingValues,
    viewModel: ConverterViewModel = hiltViewModel(),
) {
    val converterUiState by viewModel.converterUiState.collectAsStateWithLifecycle()
    val isDigitGroupingEnabled by viewModel.isDigitGroupingEnabled.collectAsStateWithLifecycle(true)

    ConverterScreen(
        contentPadding = contentPadding,
        converterUiState = converterUiState,
        isDigitGroupingEnabled = isDigitGroupingEnabled,
        onNumberSystemChanged = viewModel::convertFrom,
        onCustomRadixChanged = viewModel::updateCustomRadix,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    contentPadding: PaddingValues = PaddingValues(),
    converterUiState: ConverterUiState,
    isDigitGroupingEnabled: Boolean,
    onNumberSystemChanged: (NumberSystem) -> Unit,
    onCustomRadixChanged: (Radix) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(contentPadding)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .imePadding(),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = contentPadding.calculateTopPadding() + 8.dp,
            end = 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 72.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem10.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem10.radix)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                label = { Text(stringResource(R.string.dec)) },
                visualTransformation = if (isDigitGroupingEnabled) DigitGroupingVisualTransformation(converterUiState.numberSystem10.radix) else VisualTransformation.None,
                isError = converterUiState.numberSystem10.isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem2.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem2.radix)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                label = { Text(stringResource(R.string.bin)) },
                isError = converterUiState.numberSystem2.isError,
                visualTransformation = if (isDigitGroupingEnabled) DigitGroupingVisualTransformation(converterUiState.numberSystem2.radix) else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem8.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), converterUiState.numberSystem8.radix)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                label = { Text(stringResource(R.string.oct)) },
                isError = converterUiState.numberSystem8.isError,
                visualTransformation = if (isDigitGroupingEnabled) DigitGroupingVisualTransformation(converterUiState.numberSystem8.radix) else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            OutlinedTextField(
                value = converterUiState.numberSystem16.value,
                onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystem16.radix)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                label = { Text(stringResource(R.string.hex)) },
                isError = converterUiState.numberSystem16.isError,
                visualTransformation = if (isDigitGroupingEnabled) DigitGroupingVisualTransformation(converterUiState.numberSystem16.radix) else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done,
                ),
                shape = MaterialTheme.shapes.medium,
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = converterUiState.numberSystemCustom.value,
                    onValueChange = { textFieldValue -> onNumberSystemChanged(NumberSystem(value = textFieldValue.replace(COMMA, NS_DELIMITER), radix = converterUiState.numberSystemCustom.radix)) },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontFamily = RobotoMonoFamily),
                    label = { Text(stringResource(R.string.radix, converterUiState.numberSystemCustom.radix.value)) },
                    isError = converterUiState.numberSystemCustom.isError,
                    visualTransformation = if (isDigitGroupingEnabled) DigitGroupingVisualTransformation(converterUiState.numberSystemCustom.radix) else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrectEnabled = false,
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
                        onCustomRadixChanged(radix)
                        expanded = false
                    },
                    radix = converterUiState.numberSystemCustom.radix,
                    radixes = converterUiState.radixes,
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
private fun PreviewConverterScreen() {
    NumberSystemsTheme {
        Surface {
            ConverterScreen(
                converterUiState = ConverterUiState(),
                isDigitGroupingEnabled = true,
                onNumberSystemChanged = {},
            ) {}
        }
    }
}
