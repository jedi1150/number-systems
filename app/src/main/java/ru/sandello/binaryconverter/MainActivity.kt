package ru.sandello.binaryconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.decision_sheet.*
import kotlinx.android.synthetic.main.decision_sheet.view.*
import kotlinx.android.synthetic.main.fragment_converter.*

var cleared: Boolean = true
var clearedCalc: Boolean = true

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


//    private var mInterstitialAd: InterstitialAd? = null

    @ExperimentalUnsignedTypes
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ru.sandello.binaryconverter.R.layout.activity_main)

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
                if ((p0!!.text == getString(R.string.Converter))) {
                    converterFab.hide()
//                    (viewPager.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(viewPager.windowToken, 0)
                }
                if ((p0.text == getString(R.string.Calculator))) {
                    calculatorFab.hide()
//                    (viewPager.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(viewPager.windowToken, 0)
                }
                if ((p0.text != getString(R.string.Converter) && !cleared))
                    converterFab.show()
                if ((p0.text != getString(R.string.Calculator) && !clearedCalc))
                    calculatorFab.show()

            }
            override fun onTabSelected(p0: TabLayout.Tab?) {
            }
        })


//        mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd!!.adUnitId = "ca-app-pub-3591700046184217/4193658783"
//        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

    }


    public override fun onResume() {
        super.onResume()
//        if (mInterstitialAd!!.isLoaded) {
//            mInterstitialAd!!.show()
//        }
    }
}
