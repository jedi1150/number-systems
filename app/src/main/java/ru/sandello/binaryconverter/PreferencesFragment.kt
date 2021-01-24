package ru.sandello.binaryconverter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_preferences.*


class PreferencesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener { v, insets ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                container.updatePadding(top = insets.getInsets(WindowInsets.Type.statusBars()).top)
            }
            insets
        }
        parentFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment()).commit()
    }
}