package ru.sandello.binaryconverter

//import com.google.android.gms.ads.interstitial.InterstitialAd
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
class MainActivity : ComponentActivity() {
//    private lateinit var ad: InterstitialAd

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

//        Shared.resourcesHelper = ResourcesHelper(applicationContext)
//        Shared.preferencesHelper = PreferencesHelper(applicationContext)

        /*val adDialog = AlertDialog.Builder(this)
        adDialog.setTitle(getString(R.string.ad))
        adDialog.setMessage(getString(R.string.adMessage))
        adDialog.setCancelable(false)
        adDialog.setPositiveButton(getString(R.string.adWatch)) { _, _ ->
            ad.show(this)
        }
        adDialog.setNeutralButton(android.R.string.cancel) { _, _ ->
        }

        val adRequest = AdRequest.Builder().build()

        MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(listOf("E391F97E4B9B64A011FDEE11C58AEECF")).build())
        InterstitialAd.load(this, "ca-app-pub-3591700046184217/4193658783", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("adMob", adError.message)
                ad
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("adMob", "Ad was loaded.")
                ad = interstitialAd
                GlobalScope.launch(Dispatchers.Main) {
                    delay(DurationUnit.SECONDS.toMillis(30))
                    adDialog.show()
                }
            }
        })*/
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
