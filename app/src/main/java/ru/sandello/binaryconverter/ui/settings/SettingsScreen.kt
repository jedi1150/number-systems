package ru.sandello.binaryconverter.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.settings.components.SettingsThemeDialog
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsUiState = settingsUiState,
        onChangeThemeType = viewModel::updateThemeType,
//        onChangeLanguage = {},
    )
}

@Composable
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onChangeThemeType: (ThemeType) -> Unit,
) {
    var showThemeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            onDismiss = { showThemeDialog = false },
            settingsUiState = settingsUiState,
            onChangeThemeType = onChangeThemeType,
        )
    }

    Column {
        ListItem(
            headlineContent = {
                Text(text = stringResource(id = R.string.settings_theme))
            },
            modifier = Modifier.clickable {
                showThemeDialog = true
            },
            supportingContent = {
                Text(
                    text = when (settingsUiState.themeType) {
                        ThemeType.SYSTEM -> stringResource(id = R.string.theme_system_default)
                        ThemeType.LIGHT -> stringResource(id = R.string.theme_light)
                        ThemeType.DARK -> stringResource(id = R.string.theme_dark)
                    }
                )
            },
        )
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    NumberSystemsTheme {
        SettingsScreen(
            settingsUiState = SettingsUiState(),
            onChangeThemeType = {},
        )
    }
}
