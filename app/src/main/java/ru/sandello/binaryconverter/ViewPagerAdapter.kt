package ru.sandello.binaryconverter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val count = 2

    @ExperimentalUnsignedTypes
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ConverterFragment()
            1 -> fragment = CalculatorFragment()
        }
        return fragment
    }

    override fun getCount(): Int {
        return count
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Converter"
            1 -> return "Calculator"
        }
        return null
    }
}
