package ru.sandello.binaryconverter.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import ru.sandello.binaryconverter.ui.calculator.navigation.calculatorRoute
import ru.sandello.binaryconverter.ui.calculator.navigation.navigateToCalculator
import ru.sandello.binaryconverter.ui.converter.navigation.converterRoute
import ru.sandello.binaryconverter.ui.converter.navigation.navigateToConverter
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CALCULATOR
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CONVERTER
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.SETTINGS
import ru.sandello.binaryconverter.ui.settings.navigation.navigateToSettings
import ru.sandello.binaryconverter.ui.settings.navigation.settingsRoute

@Composable
fun rememberNumSysAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NumSysAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
    ) {
        NumSysAppState(
            navController,
            windowSizeClass,
        )
    }
}

@Stable
class NumSysAppState(
    val navController: NavHostController,
    private val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            converterRoute -> CONVERTER
            calculatorRoute -> CALCULATOR
            settingsRoute -> SETTINGS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            CONVERTER -> navController.navigateToConverter(topLevelNavOptions)
            CALCULATOR -> navController.navigateToCalculator(topLevelNavOptions)
            SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
        }
    }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
}