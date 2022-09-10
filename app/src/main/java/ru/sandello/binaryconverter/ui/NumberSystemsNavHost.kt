package ru.sandello.binaryconverter.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.main.MainRoute

@Composable
fun NumberSystemsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "numsys") {
        composable("numsys") {
            MainRoute()
        }
        composable("settings") {}
    }
}
