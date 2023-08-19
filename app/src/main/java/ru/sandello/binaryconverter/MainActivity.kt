package ru.sandello.binaryconverter

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.data.ThemeType
import ru.sandello.binaryconverter.ui.NumberSystemsApp
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { state ->
                        uiState = state
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainUiState.Loading -> true
                is MainUiState.Success -> false
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                    ) { darkTheme },
                )
                onDispose {}
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
): Boolean = when (uiState) {
    MainUiState.Loading -> isSystemInDarkTheme()
    is MainUiState.Success -> when (uiState.settings.themeType) {
        ThemeType.SYSTEM -> isSystemInDarkTheme()
        ThemeType.LIGHT -> false
        ThemeType.DARK -> true
    }
}
