package ru.sandello.binaryconverter.ui.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.sandello.binaryconverter.ui.navigation.SettingsRoute
import ru.sandello.binaryconverter.ui.settings.SettingsRoute as SettingsRouteScreen

fun EntryProviderScope<NavKey>.settingsScreen(contentPadding: PaddingValues) {
    entry<SettingsRoute> {
        SettingsRouteScreen(contentPadding = contentPadding)
    }
}
