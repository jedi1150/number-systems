package ru.sandello.binaryconverter

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.updatePadding
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_main.view.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener { it, insets ->
            it.updatePadding(bottom = view.rootView.bottom_navigation.height, right = insets.systemWindowInsetRight, left = insets.systemWindowInsetLeft)
            insets
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val themePref = findPreference("theme") as ListPreference?
        val languagePref = findPreference("language") as ListPreference?
        val commaPref = findPreference("comma") as ListPreference?
        val translatePref = findPreference("translate") as Preference?
        val githubPref = findPreference("github") as Preference?

        var themeEntries = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.battery_saver))
        val themeEntryValues = arrayOf("0", "1", "2")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            themePref?.setOnPreferenceChangeListener { _, newValue ->
                when (newValue.toString()) {
                    "0" -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    "1" -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    "2" -> setTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
                true
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            themeEntries = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.system_default))
            themePref?.setOnPreferenceChangeListener { _, newValue ->
                when (newValue.toString()) {
                    "0" -> setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    "1" -> setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    "2" -> setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                true
            }
        }
        themePref?.entries = themeEntries
        themePref?.entryValues = themeEntryValues
        if (themePref?.value == null)
            themePref?.setValueIndex(2)



        val languageEntries = resources.getStringArray(R.array.language_array)
        val languageEntryValues = resources.getStringArray(R.array.language_locale_array)
        languagePref?.entries = languageEntries
        languagePref?.entryValues = languageEntryValues
        val commaEntries = resources.getStringArray(R.array.comma_array)
        val commaEntryValues = resources.getStringArray(R.array.comma_array)
        commaPref?.setOnPreferenceChangeListener { _, newValue ->
            fractionCount = newValue.toString().toInt()
            true
        }
        commaPref?.entries = commaEntries
        commaPref?.entryValues = commaEntryValues
        if (commaPref?.value == null)
            commaPref?.value = "12"

        translatePref?.setOnPreferenceClickListener {
            val url = "https://app.lokalise.com/project/500055995e428d3e42ff98.41853582/?view=multi"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
            true
        }

        githubPref?.setOnPreferenceClickListener {
            val url = "https://github.com/jedi1150/Number-Systems"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
            true
        }
    }

    private fun setTheme(themeMode: Int) {
        (activity as AppCompatActivity).delegate.localNightMode = themeMode
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}
