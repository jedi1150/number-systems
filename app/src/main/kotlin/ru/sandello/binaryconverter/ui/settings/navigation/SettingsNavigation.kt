package ru.sandello.binaryconverter.ui.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.navigation.SettingsRoute
import ru.sandello.binaryconverter.ui.settings.SettingsRoute as SettingsRouteScreen

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SettingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(contentPadding: PaddingValues) {
    composable<SettingsRoute> {
        SettingsRouteScreen(contentPadding = contentPadding)
    }
}
