package ru.sandello.binaryconverter

import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import java.util.*


class SettingsFragment2 : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val themePref = findPreference("theme") as ListPreference?
        val languagePref = findPreference("language") as ListPreference?
        val commaPref = findPreference("comma") as ListPreference?

        var themeEntries = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.battery_saver))
        val themeEntryValues = arrayOf("0", "1", "2")
        var languageEntries = resources.getStringArray(R.array.language_array)
        val languageEntryValues = resources.getStringArray(R.array.language_locale_array)
        val commaEntries = resources.getStringArray(R.array.comma_array)
        val commaEntryValues = resources.getStringArray(R.array.comma_array)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            themeEntries = arrayOf(getString(R.string.light), getString(R.string.dark), getString(R.string.system_default))
        }
        themePref?.entries = themeEntries
        themePref?.entryValues = themeEntryValues

        languagePref?.entries = languageEntries
        languagePref?.entryValues = languageEntryValues

        commaPref?.entries = commaEntries
        commaPref?.entryValues = commaEntryValues
    }

}
