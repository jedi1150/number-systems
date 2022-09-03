package ru.sandello.binaryconverter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@Composable
fun NumberSystemsApp() {
    val navController = rememberNavController()

    NumberSystemsTheme {
        NumberSystemsNavHost(navController = navController)
    }
}
