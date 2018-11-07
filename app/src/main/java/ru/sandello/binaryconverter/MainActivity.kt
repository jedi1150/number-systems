package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_explanation.*


var cleared: Boolean = true

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

private var themeMode = "Light"

    @SuppressLint("CommitTransaction")
    @ExperimentalUnsignedTypes
    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        setContentView(ru.sandello.binaryconverter.R.layout.activity_main)

        val listTheme = resources.getStringArray(R.array.themeNames)

        toolbar.setOnMenuItemClickListener { item ->
            var i = 0
            when (item!!.itemId) {
                R.id.nightButton -> {
                    when (themeMode)
                    {
                        "Light" -> i = 1
                        "Dark" -> i = 2
                    }
                    val sharedPref = getSharedPreferences("theme", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("theme", listTheme[i]).apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finishFromChild(MainActivity())
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> {
                    false
                }
            }
        }
        toolbar.inflateMenu(R.menu.menu)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupBottomNavMenu(navController)

        bottom_nav.setOnNavigationItemReselectedListener {  }

        main_overlay.setOnClickListener {
            val bottomS = BottomSheetBehavior.from(cardViewSheet)
            bottomS.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val bottomS = BottomSheetBehavior.from(cardViewSheet)
        if (bottomS.state == BottomSheetBehavior.STATE_HIDDEN) {
            finish()
            moveTaskToBack(false)
        }
        if (bottomS.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomS.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()


    @SuppressLint("ResourceType")
    private fun setTheme()
    {
        val themePref = getSharedPreferences("theme", Context.MODE_PRIVATE)
        if (themePref.getString("theme", "Light") == "Dark") {
            super.setTheme(R.style.DarkTheme)
            themeMode = "Dark"
        }
        if (themePref.getString("theme", "Light") == "Light") {
            super.setTheme(R.style.LightTheme)
            themeMode = "Light"
        }
    }
}
