package ru.sandello.binaryconverter.ui.settings

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.settings.components.SettingsLocaleDialog
import ru.sandello.binaryconverter.ui.settings.components.SettingsThemeDialog
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import java.util.Locale

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsUiState = settingsUiState,
        onChangeThemeType = viewModel::updateThemeType,
        onChangeLocale = { locale ->
            viewModel.updateLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (locale != Locale.ROOT) {
                    context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(locale)
                } else {
                    context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.getEmptyLocaleList()
                }
            } else {
                if (locale != Locale.ROOT) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
                } else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                }
            }
        },
    )
}

@Composable
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onChangeThemeType: (ThemeType) -> Unit,
    onChangeLocale: (Locale) -> Unit,
) {
    var showThemeDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showLocaleDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            onDismiss = { showThemeDialog = false },
            settingsUiState = settingsUiState,
            onChangeThemeType = onChangeThemeType,
        )
    }
    if (showLocaleDialog) {
        SettingsLocaleDialog(
            onDismiss = { showLocaleDialog = false },
            settingsUiState = settingsUiState,
            onChangeLocale = onChangeLocale,
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
        ListItem(
            headlineContent = {
                Text(text = stringResource(id = R.string.settings_language))
            },
            modifier = Modifier.clickable {
                showLocaleDialog = true
            },
            supportingContent = {
                val currentLocale = settingsUiState.availableLocales.entries.firstOrNull { it.key == settingsUiState.locale.toLanguageTag() }
                Text(text = when (settingsUiState.locale.toLanguageTag()) {
                    currentLocale?.key -> currentLocale?.value?.let { stringResource(id = it) } ?: stringResource(id = R.string.locale_system)
                    else -> stringResource(id = R.string.locale_system)
                })
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
            onChangeLocale = {},
        )
    }
}
