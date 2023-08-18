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
import ru.sandello.binaryconverter.ui.settings.SettingsUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsLanguageDialog(
    onDismiss: () -> Unit,
    settingsUiState: SettingsUiState,
    onChangeLocale: (Locale) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
    ) {
        var selectedLocale by rememberSaveable {
            mutableStateOf(settingsUiState.locale)
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
                SettingsDialogTitle(stringResource(R.string.settings_language))
                Column(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                ) {
                    settingsUiState.availableLocales.forEach { locale ->
                        val language = if (locale != Locale.ROOT) {
                            locale.getDisplayLanguage(locale).replaceFirstChar { letter -> if (letter.isLowerCase()) letter.titlecase(locale) else letter.toString() }
                        } else {
                            stringResource(id = R.string.locale_system)
                        }
                        SettingsDialogThemeChooserRow(
                            text = language,
                            selected = selectedLocale == locale,
                            onClick = { selectedLocale = locale },
                        )
                    }
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
                            onChangeLocale(selectedLocale)
                        },
                    ) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                }
            }
        }
    }
}
