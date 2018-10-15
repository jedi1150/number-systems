package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.explanation_sheet.*

var cleared: Boolean = true
var clearedCalc: Boolean = true

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

private var themeMode = "Light"

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
                        "Black" -> i = 0
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

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.run {
            icon = getDrawable(R.drawable.ic_round_loop_24px)
            text = getString(R.string.Converter)
        }
        tabs.getTabAt(1)?.run {
            icon = getDrawable(R.drawable.ic_calculator)
            text = getString(R.string.Calculator)
        }


        tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {
                if (p0!!.text == getString(R.string.Converter)) {
                    converterFab.hide()
                    explanationFab.hide()
//                    (viewPager.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(viewPager.windowToken, 0)
                }
                if (p0.text == getString(R.string.Calculator)) {
                    calculatorFab.hide()
//                    (viewPager.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(viewPager.windowToken, 0)
                }
                if (p0.text != getString(R.string.Converter) && !cleared) {
                    converterFab.show()
                    explanationFab.show()
                }
                if (p0.text != getString(R.string.Calculator) && !clearedCalc)
                    calculatorFab.show()

            }
            @SuppressLint("ResourceType")
            override fun onTabSelected(p0: TabLayout.Tab?) {}
        })

        val tabSave = getSharedPreferences("main", Context.MODE_PRIVATE)
        tabs.getTabAt(tabSave.getInt("tab", 0))?.select()


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

    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("tab", tabs.selectedTabPosition).apply()
    }

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
//        if (themePref.getString("theme", "Light") == "Black") {
//            super.setTheme(R.style.BlackTheme)
//            themeMode = "Black"
//        }
    }
}
