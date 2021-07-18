package ru.sandello.binaryconverter.ui.calculator

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.ActivityMainBinding
import ru.sandello.binaryconverter.databinding.FragmentCalculatorBinding
import ru.sandello.binaryconverter.ui.main.MainActivity
import ru.sandello.binaryconverter.ui.main.MainViewModel
import ru.sandello.binaryconverter.utils.CalcActions
import ru.sandello.binaryconverter.utils.Format
import ru.sandello.binaryconverter.utils.TypeMethod

class CalculatorFragment : Fragment() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var binding: FragmentCalculatorBinding
    private val model: CalculatorViewModel by navGraphViewModels(R.id.nav_graph)
    private val activityViewModel: MainViewModel by activityViewModels()

    private var myClipboard: ClipboardManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activityBinding = (requireActivity() as MainActivity).binding
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myClipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        binding.editTextResult.setOnClickListener { model.copyVal("calResult") }

        activityBinding.clearFab.setOnClickListener {
            binding.editTextVal1.setText("")
            binding.editTextVal2.setText("")
            binding.editTextResult.setText("")
            activityBinding.clearFab.hide()
        }

        model.stringToast.observe(viewLifecycleOwner, {
            Snackbar.make(
                activityBinding.snackbar,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        })

//        TypeMethod().type(
//            binding.editTextVal1,
//            model.allow(binding.spinner1.text.toString().toInt())
//        )
//        TypeMethod().type(
//            binding.editTextVal2,
//            model.allow(binding.spinner2.text.toString().toInt())
//        )

        binding.spinner1.setOnItemClickListener { _, _, _, _ ->
            TypeMethod().type(
                binding.editTextVal1,
                model.allow(binding.spinner1.text.toString().toInt())
            )
//            calculate(model.allow(binding.spinner1))
        }

        binding.spinner2.setOnItemClickListener { _, _, _, _ ->
            TypeMethod().type(
                binding.editTextVal2,
                model.allow(binding.spinner2.text.toString().toInt())
            )
//            calculate(model.allow(binding.spinner2))
        }

//        binding.spinner3.setOnItemClickListener { _, _, _, _ ->
////            calculate("")
//            model.updateFraction(3, )
//        }
        binding.spinner1.setOnItemClickListener { _, _, i, _ ->
            model.updateFraction(1, i)
        }

        binding.spinner2.setOnItemClickListener { _, _, i, _ ->
            model.updateFraction(2, i)
        }

        binding.spinner3.setOnItemClickListener { _, _, i, _ ->
            model.updateFraction(3, i)
        }

        binding.editTextVal1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.editTextVal1.removeTextChangedListener(this)
                Format().format(binding.editTextVal1)
                binding.editTextVal1.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                calculate(model.allow(binding.spinner1))
                model.updateOperand(
                    action = 1,
                    value = s.toString(),
                    fraction = binding.spinner1.text.toString().toInt(),
                )
            }
        })


        binding.editTextVal2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.editTextVal2.removeTextChangedListener(this)
                Format().format(binding.editTextVal2)
                binding.editTextVal2.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                calculate(model.allow(binding.spinner2))
                model.updateOperand(
                    action = 1,
                    value = s.toString(),
                    fraction = binding.spinner1.text.toString().toInt(),
                )
            }
        })

//        binding.toggleGroup.addOnButtonCheckedListener { _, _, _ ->
//            calculate("")
//        }

        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                1 -> model.updateAction(CalcActions.PLUS)
                2 -> model.updateAction(CalcActions.MINUS)
                3 -> model.updateAction(CalcActions.MULTIPLY)
                4 -> model.updateAction(CalcActions.DIVIDE)
            }
        }

        model.showInvalidInputError.observe(viewLifecycleOwner, {
            if (it.first == 1) {
                binding.editTextVal1.error =
                    "${getString(R.string.available_characters_for_input)}: ${it.second}"
            } else if (it.first == 2) {
                binding.editTextVal2.error =
                    "${getString(R.string.available_characters_for_input)}: ${it.second}"
            }
        })
    }
//
//    fun calculate(allowVal: String) {
//        checkClear()
//        errorNull()
//        if (binding.editTextVal1.text.toString().isNotEmpty())
//            try {
//                model.updateOperand(
//                    action = 1,
//                    value = binding.editTextVal1.text.toString(),
//                    fraction = binding.spinner1.text.toString().toInt(),
//                )
//            } catch (e: Exception) {
//                binding.textInputLayoutCustom1.error =
//                    Shared.resourceProvider.getString(R.string.available_characters_for_input) + ": $allowVal"
//                e.printStackTrace()
//            }
//        if (binding.editTextVal2.text.toString().isNotEmpty())
//            try {
//                model.updateOperand(
//                    action = 2,
//                    value = binding.editTextVal2.text.toString(),
//                    fraction = binding.spinner2.text.toString().toInt(),
//                )
//            } catch (e: Exception) {
//                binding.textInputLayoutCustom2.error =
//                    Shared.resourceProvider.getString(R.string.available_characters_for_input) + ": $allowVal"
//                e.printStackTrace()
//            }
//        save()
//    }

    private fun showError(textView: TextInputEditText, text: String) {
        textView.error = text
        textView.requestFocus()
    }


    override fun onStart() {
        super.onStart()
        model.load()
    }

    override fun onPause() {
        super.onPause()
        model.save()
    }
}
