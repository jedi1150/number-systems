package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.unit.Duration
import androidx.compose.ui.unit.inMilliseconds
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


var fractionCount = 12

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNightMode()
        setContentView(R.layout.activity_main)
        main_container.setOnApplyWindowInsetsListener { _, insets ->
            bottom_navigation.updatePadding(bottom = insets.systemWindowInsetBottom, right = insets.systemWindowInsetRight, left = insets.systemWindowInsetLeft)
            insets
        }

        val themePref = getSharedPreferences("decLength", Context.MODE_PRIVATE)
        fractionCount = themePref.getInt("decLength", 12)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupBottomNavMenu(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label.toString() == "Settings") {
                clear_fab.hide()
                explanation_fab.hide()
                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(main_container.windowToken, 0)
            }
            if (destination.label.toString() == "Calculator") {
                explanation_fab.hide()
            }
        }

        bottom_navigation.setOnNavigationItemReselectedListener { }

        val adDialog = AlertDialog.Builder(this)
        adDialog.setTitle(getString(R.string.ad))
        adDialog.setMessage(getString(R.string.adMessage))
        adDialog.setCancelable(false)
        adDialog.setPositiveButton(getString(R.string.adWatch)) { _, _ ->
            mInterstitialAd.show(this)
        }
        adDialog.setNeutralButton(android.R.string.cancel) { _, _ ->
        }

        val adRequest = AdRequest.Builder().build()

        MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("E391F97E4B9B64A011FDEE11C58AEECF")).build())
        InterstitialAd.load(this, "ca-app-pub-3591700046184217/4193658783", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("adMob", adError.message)
                mInterstitialAd
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("adMob", "Ad was loaded.")
                mInterstitialAd = interstitialAd
                GlobalScope.launch(Dispatchers.Main) {
                    delay(Duration(seconds = 30).inMilliseconds())
                    adDialog.show()
                }
            }
        })
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_navigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()


    @SuppressLint("InlinedApi")
    private fun setNightMode() {
        val isNightMode = this.resources.configuration.uiMode
                .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
        if (!isNightMode) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            xor View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
        }
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getString("theme", "2") == "1") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
        if (sharedPreferences.getString("theme", "2") == "0") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }

        if (sharedPreferences.getString("theme", "2") == "2") {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    }

}
