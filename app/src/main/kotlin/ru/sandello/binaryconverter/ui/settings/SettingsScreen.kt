package ru.sandello.binaryconverter.ui.settings

import android.app.LocaleManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.settings.components.SettingsLanguageDialog
import ru.sandello.binaryconverter.ui.settings.components.SettingsThemeDialog
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.utils.GITHUB_URL
import ru.sandello.binaryconverter.utils.PRIVACY_POLICY_URL
import java.util.Locale
import androidx.core.net.toUri

@Composable
fun SettingsRoute(contentPadding: PaddingValues, viewModel: SettingsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
    val appVersion = stringResource(R.string.app_version, packageInfo.versionName.orEmpty(), PackageInfoCompat.getLongVersionCode(packageInfo))

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

    SettingsScreen(
        contentPadding = contentPadding,
        settingsUiState = settingsUiState,
        appVersion = appVersion,
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
        onChangeDigitGrouping = viewModel::updateDigitGrouping,
        onLinkClicked = { url ->
            launchCustomChromeTab(context, url.toUri(), backgroundColor)
        },
    )
}

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues = PaddingValues(),
    settingsUiState: SettingsUiState,
    appVersion: String,
    onChangeThemeType: (ThemeType) -> Unit,
    onChangeLocale: (Locale) -> Unit,
    onChangeDigitGrouping: (Boolean) -> Unit,
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

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    PaddingValues(
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                ),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
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
                HorizontalDivider()
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.digit_grouping))
                    },
                    modifier = Modifier.clickable {
                        onChangeDigitGrouping(settingsUiState.isDigitGroupingEnabled.not())
                    },
                    supportingContent = {
                        Text(stringResource(R.string.digit_grouping_description))
                    },
                    trailingContent = {
                        Switch(
                            checked = settingsUiState.isDigitGroupingEnabled,
                            onCheckedChange = {
                                onChangeDigitGrouping(it)
                            },
                        )
                    },
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.settings_github))
                    },
                    modifier = Modifier.clickable {
                        onLinkClicked(GITHUB_URL)
                    },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.settings_contribute_description),
                        )
                    },
                )
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = R.string.settings_privacy_policy))
                    },
                    modifier = Modifier.clickable {
                        onLinkClicked(PRIVACY_POLICY_URL)
                    },
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = appVersion,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
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

@Preview(device = "spec:width=411dp,height=891dp", wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:width=411dp,height=891dp", wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun PreviewSettingScreen() {
    NumberSystemsTheme {
        SettingsScreen(
            settingsUiState = SettingsUiState(
                themeType = ThemeType.SYSTEM,
                locale = Locale.ROOT,
                isDigitGroupingEnabled = true,
            ),
            appVersion = "versionName",
            onChangeThemeType = {},
            onChangeLocale = {},
            onChangeDigitGrouping = {},
            onLinkClicked = {},
        )
    }
}
