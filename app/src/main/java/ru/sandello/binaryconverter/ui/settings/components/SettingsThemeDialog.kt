package ru.sandello.binaryconverter.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.settings.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsThemeDialog(
    onDismiss: () -> Unit,
    settingsUiState: SettingsUiState,
    onChangeThemeType: (ThemeType) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
    ) {
        var selectedTheme by rememberSaveable {
            mutableStateOf(settingsUiState.themeType)
        }

        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .selectableGroup(),
            ) {
                SettingsDialogTitle(stringResource(R.string.settings_theme))
                Column(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                ) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.theme_system_default),
                        selected = selectedTheme == ThemeType.SYSTEM,
                        onClick = { selectedTheme = ThemeType.SYSTEM },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.theme_light),
                        selected = selectedTheme == ThemeType.LIGHT,
                        onClick = { selectedTheme = ThemeType.LIGHT },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(id = R.string.theme_dark),
                        selected = selectedTheme == ThemeType.DARK,
                        onClick = { selectedTheme = ThemeType.DARK },
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.End,
                    )
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = android.R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            onDismiss()
                            onChangeThemeType(selectedTheme)
                        },
                    ) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                }
            }
        }
    }
}
