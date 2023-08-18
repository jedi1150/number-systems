package ru.sandello.binaryconverter.ui.settings

import android.app.LocaleManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.settings.components.SettingsLanguageDialog
import ru.sandello.binaryconverter.ui.settings.components.SettingsThemeDialog
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.GITHUB_URL
import java.util.Locale

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

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
        onLinkClicked = { url ->
            launchCustomChromeTab(context, Uri.parse(url), backgroundColor)
        },
    )
}

@Composable
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onChangeThemeType: (ThemeType) -> Unit,
    onChangeLocale: (Locale) -> Unit,
    onLinkClicked: (String) -> Unit,
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
        SettingsLanguageDialog(
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
                val currentLocale = settingsUiState.availableLocales.firstOrNull { locales -> locales == settingsUiState.locale }
                val language = if (currentLocale != null && currentLocale != Locale.ROOT) {
                    currentLocale.getDisplayLanguage(currentLocale).replaceFirstChar { letter -> if (letter.isLowerCase()) letter.titlecase(currentLocale) else letter.toString() }
                } else {
                    stringResource(id = R.string.locale_system)
                }
                Text(
                    text = when (settingsUiState.locale) {
                        currentLocale -> language
                        else -> stringResource(id = R.string.locale_system)
                    },
                )
            },
        )
        Divider()
        ListItem(
            headlineContent = {
                Text(text = stringResource(id = R.string.settings_github))
            },
            modifier = Modifier.clickable {
                onLinkClicked(GITHUB_URL)
            },
        )
    }
}

private fun launchCustomChromeTab(context: Context, uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor).build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()

    customTabsIntent.launchUrl(context, uri)
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    NumberSystemsTheme {
        SettingsScreen(
            settingsUiState = SettingsUiState(),
            onChangeThemeType = {},
            onChangeLocale = {},
            onLinkClicked = {},
        )
    }
}
