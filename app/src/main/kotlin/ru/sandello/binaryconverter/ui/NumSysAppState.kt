package ru.sandello.binaryconverter.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import ru.sandello.binaryconverter.ui.navigation.CalculatorRoute
import ru.sandello.binaryconverter.ui.navigation.ConverterRoute
import ru.sandello.binaryconverter.ui.navigation.NavigationState
import ru.sandello.binaryconverter.ui.navigation.Navigator
import ru.sandello.binaryconverter.ui.navigation.SettingsRoute
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CALCULATOR
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CONVERTER
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.SETTINGS
import ru.sandello.binaryconverter.ui.navigation.rememberNavigationState

@Composable
fun rememberNumSysAppState(
    windowSizeClass: WindowSizeClass,
): NumSysAppState {
    val navigationState = rememberNavigationState(
        startRoute = ConverterRoute,
        topLevelRoutes = setOf(ConverterRoute, CalculatorRoute, SettingsRoute),
    )
    val navigator = remember { Navigator(navigationState) }
    return remember(
        navigationState,
        navigator,
        windowSizeClass,
    ) {
        NumSysAppState(
            navigationState = navigationState,
            navigator = navigator,
            windowSizeClass = windowSizeClass,
        )
    }
}

@Stable
class NumSysAppState(
    val navigationState: NavigationState,
    val navigator: Navigator,
    private val windowSizeClass: WindowSizeClass,
) {
    val currentTopLevelDestination: TopLevelDestination?
        get() = when (navigationState.topLevelRoute) {
            ConverterRoute -> CONVERTER
            CalculatorRoute -> CALCULATOR
            SettingsRoute -> SETTINGS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navigator.navigate(topLevelDestination.route)
    }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
}
