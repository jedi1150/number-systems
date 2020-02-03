package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val nightModePref by lazy { context!!.getSharedPreferences("nightMode", Context.MODE_PRIVATE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        textViewComma.text = getString(R.string.decLengthText) + ": " + (fractionCount)

        settingsTheme.setOnClickListener {
            var items = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.battery_saver))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                MaterialAlertDialogBuilder(this.context)
                        .setTitle(getString(R.string.chooseTheme))
                        .setSingleChoiceItems(items, nightModePref.getInt("nightMode", 2)) { dialogInterface, i ->
                            when (i) {
                                0 -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, 0)
                                1 -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, 1)
                                2 -> setTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY, 2)
                            }
                            dialogInterface.cancel()
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.cancel() }
                        .show()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                items = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.system_default))
                MaterialAlertDialogBuilder(this.context)
                        .setTitle(getString(R.string.chooseTheme))
                        .setSingleChoiceItems(items, nightModePref.getInt("nightMode", 2)) { dialogInterface, i ->
                            when (i) {
                                0 -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, 0)
                                1 -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, 1)
                                2 -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, 2)
                            }
                            dialogInterface.cancel()
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.cancel() }
                        .show()
            }
        }
    }

//        seekBarComma.value = (fractionCount).toFloat()
//        seekBarComma.addOnChangeListener { slider, value, fromUser ->
//
//            fractionCount = (value).toInt()
//            textViewComma.text = getString(R.string.decLengthText) + ": " + (value.toInt())
//            val sharedPref = context!!.getSharedPreferences("decLength", Context.MODE_PRIVATE)
//            val editor = sharedPref.edit()
//            editor.putInt("decLength", (value).toInt()).apply()
//        }
//    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        try {
            (activity as AppCompatActivity).delegate.localNightMode = themeMode
            saveTheme(prefsMode)
        } catch (e: Exception) {
        }
    }

    private fun saveTheme(theme: Int) = nightModePref.edit().putInt("nightMode", theme).apply()

}