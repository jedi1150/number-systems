package ru.sandello.binaryconverter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.main.MainScreen

@Composable
fun NumberSystemsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "numsys") {
        composable("numsys") {
            MainScreen()
        }
        composable("settings") {}
    }
}
