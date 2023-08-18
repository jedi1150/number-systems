package ru.sandello.binaryconverter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.NumberSystemsApp
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
            }

            NumberSystemsTheme(
                darkTheme = darkTheme,
            ) {
                NumberSystemsApp(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
        }
    }

}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainUiState,
): Boolean = when (uiState.settings.themeType) {
    ThemeType.SYSTEM -> isSystemInDarkTheme()
    ThemeType.LIGHT -> false
    ThemeType.DARK -> true
}
