package ru.sandello.binaryconverter.ui.settings

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.updatePadding
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.ActivityMainBinding
import ru.sandello.binaryconverter.ui.main.MainActivity
import ru.sandello.binaryconverter.utils.Shared

@Suppress("DEPRECATION")
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityBinding = (requireActivity() as MainActivity).binding

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener { v, insets ->
            view.post {
                v.updatePadding(top = insets.systemWindowInsetTop, bottom = activityBinding.bottomNavigation.height)
            }
            insets
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val themePref = findPreference("theme") as ListPreference?
        val languagePref = findPreference("language") as ListPreference?
        val commaPref = findPreference("comma") as ListPreference?
        val reviewPref = findPreference("review") as Preference?
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
            Shared.FRACTION_COUNT = newValue.toString().toInt()
            true
        }
        commaPref?.entries = commaEntries
        commaPref?.entryValues = commaEntryValues
        if (commaPref?.value == null)
            commaPref?.value = "12"

        reviewPref?.setOnPreferenceClickListener {
            val reviewManager = ReviewManagerFactory.create(requireContext())
            val requestReviewFlow = reviewManager.requestReviewFlow()
            requestReviewFlow.addOnCompleteListener { request ->
                if (request.isSuccessful) {
                    val reviewInfo = request.result
                    val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                    flow.addOnCompleteListener {
                        // Обрабатываем завершение сценария оценки
                    }
                }
            }
            true
        }

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
