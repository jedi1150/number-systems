package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.sandello.binaryconverter.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    private val nightModePref by lazy { context!!.getSharedPreferences("nightMode", Context.MODE_PRIVATE) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding!!.comma = "${getString(R.string.decLengthText)}: $fractionCount"
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        settingsComma.setOnClickListener {
            val items = arrayOf("12", "13", "14", "15", "16", "17", "18", "19", "20")
            val sharedPref = context!!.getSharedPreferences("decLength", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            MaterialAlertDialogBuilder(this.context)
                    .setTitle(getString(R.string.decLengthText))
                    .setSingleChoiceItems(items, items.indexOf(sharedPref.getInt("decLength", 12).toString())) { dialogInterface, i ->
                        editor.putInt("decLength", items[i].toInt()).apply()
                        fractionCount = items[i].toInt()
                        binding!!.comma = "${getString(R.string.decLengthText)}: $fractionCount"
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.cancel() }
                    .show()
        }
    }

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