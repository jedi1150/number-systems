package ru.sandello.binaryconverter.ui.settings.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.settings.SettingsRoute

const val settingsRoute = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(contentPadding: PaddingValues) {
    composable(
        route = settingsRoute,
    ) {
        SettingsRoute(contentPadding = contentPadding)
    }
}
