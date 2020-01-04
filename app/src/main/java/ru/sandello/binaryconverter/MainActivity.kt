package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

var fractionCount = 12

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    @SuppressLint("CommitTransaction")
    @ExperimentalUnsignedTypes
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNightMode()
        setContentView(R.layout.activity_main)

        main_container.setOnApplyWindowInsetsListener { _, insets ->
            bottom_navigation.updatePadding(bottom = insets.systemWindowInsetBottom, right = insets.systemWindowInsetRight, left = insets.systemWindowInsetLeft)
            fab_group.updatePadding(right = insets.systemWindowInsetRight, left = insets.systemWindowInsetLeft)
            insets
        }

        val themePref = getSharedPreferences("decLength", Context.MODE_PRIVATE)
        fractionCount = themePref.getInt("decLength", 12)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupBottomNavMenu(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label.toString() == "Settings") {
                clear_fab.hide()
                explanation_fab.hide()
            }
            if (destination.label.toString() == "Calculator") {
                explanation_fab.hide()
            }
        }

        bottom_navigation.setOnNavigationItemReselectedListener { }

        val nightModeSwitched = getSharedPreferences("nightMode", Context.MODE_PRIVATE)
        val nightModeEditor = nightModeSwitched.edit()
        if (nightModeSwitched.getBoolean("nightModeSwitched", false)) {
            navController.navigate(R.id.destination_settings)
            nightModeEditor.putBoolean("nightModeSwitched", false).apply()
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_navigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()


    @SuppressLint("ResourceType", "InlinedApi") //Переключение ночного режима, при запуске приложения
    private fun setNightMode() {
        val isNightMode = this.resources.configuration.uiMode
                .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        Log.d("themeMode", isNightMode.toString())
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
        val nightModePref = getSharedPreferences("nightMode", Context.MODE_PRIVATE)
        if (nightModePref.getInt("nightMode", 2) == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
        if (nightModePref.getInt("nightMode", 2) == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }

        if (nightModePref.getInt("nightMode", 2) == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }


}
