package ru.sandello.binaryconverter.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.FragmentPreferencesBinding
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel

class PreferencesFragment : Fragment() {
    private lateinit var binding: FragmentPreferencesBinding
    private val model: CalculatorViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPreferencesBinding.inflate(layoutInflater)
//        binding.model = model
//        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnApplyWindowInsetsListener { v, insets ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                binding.container.updatePadding(top = insets.getInsets(WindowInsets.Type.statusBars()).top)
            }
            insets
        }
        parentFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment()).commit()
    }
}