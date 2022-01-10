package ru.sandello.binaryconverter.ui.converter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.ActivityMainBinding
import ru.sandello.binaryconverter.databinding.FragmentConverterBinding
import ru.sandello.binaryconverter.utils.TypeMethod

var cleared = false

class ConverterFragment : Fragment() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var binding: FragmentConverterBinding
    private val model: ConverterViewModel by navGraphViewModels(R.id.nav_graph)

    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetInternal: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        activityBinding = (requireActivity() as MainActivity).binding
        binding = FragmentConverterBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        model.load()
        bottomSheetDialog = BottomSheetDialog(view.rootView.context)
        bottomSheetDialog!!.setContentView(R.layout.fragment_explanation)
        bottomSheetInternal = bottomSheetDialog!!.findViewById(R.id.bottomSheetMain)
        bottomSheetDialog!!.behavior.skipCollapsed = true
        bottomSheetDialog!!.behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) {
                    bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                    bottomSheetDialog!!.dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        activityBinding.clearFab.setOnClickListener {
            binding.editText10.setText("")
            binding.editText2.setText("")
            binding.editText8.setText("")
            binding.editText16.setText("")
            binding.editTextCustom.setText("")

            activityBinding.clearFab.hide()
            activityBinding.explanationFab.hide()
        }

        model.operandBase.observe(viewLifecycleOwner, {
//            Log.d("test228 operandCustom observe", it.toString())
        })

//        TypeMethod().type(
//            binding.editTextCustom,
//            model.allow(binding.fractionCustom.text.toString().toInt())
//        )

        binding.fractionCustom.setOnItemClickListener { _, _, position, _ ->
            try {
                TypeMethod().type(
                    binding.editTextCustom,
                    model.allow(binding.fractionCustom.text.toString().toInt())
                )
                model.updateCustomRadix(position)
//                binding.editTextCustom.setText(
//                    ConvertTo().main(
//                        binding.editText10.text.toString(),
//                        10,
//                        listCustomBin[position]
//                    )
//                )
                binding.editTextCustom.setSelection(binding.editTextCustom.text!!.length)
                errorNull()
            } catch (e: Exception) {
                if (binding.editTextCustom.text.toString() != "")
                    binding.textInputLayoutCustom.error = getString(R.string.invalid_value)
            }
        }

        val spinnerSharedPref =
            requireContext().getSharedPreferences("spinner", Context.MODE_PRIVATE)
        val spinnerEditor = spinnerSharedPref.edit()

//        var adapter = ArrayAdapter(
//            requireContext(),
//            R.layout.dropdown_menu_popup_item,
//            listAllBin
//        )

        //10
/*        binding.editText10.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("test228 onTextChanged", s.toString())
                model.updateOperand(fraction = 10, value = s.toString())
//                if (binding.editText10.hasFocus()) {
//                    if (s.toString() != "" && !s!!.endsWith("."))
//                        parse(
//                            s.toString(),
//                            binding.editText10,
//                            binding.textInputLayout10,
//                            10,
//                            binding.fractionCustom.text.toString().toInt(),
//                            model.allow(10)
//                        )
//                    if (s.toString() == "" && !cleared)
//                        clearConverter(binding.editText10)
//                }
//                binding.editText10.removeTextChangedListener(this)
//                Format().format(binding.editText10)
//                binding.editText10.addTextChangedListener(this)
            }
        })*/
        //2
/*        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                model.updateOperand(fraction = 2, value = s.toString())
//                if (binding.editText2.hasFocus())
//                    if (s.toString() != "" && !s!!.endsWith("."))
//                        parse(
//                            s.toString(),
//                            binding.editText2,
//                            binding.textInputLayout2,
//                            2,
//                            binding.fractionCustom.text.toString().toInt(),
//                            model.allow(2)
//                        )
//                if (s.toString() == "" && !cleared)
//                    clearConverter(binding.editText2)
//                binding.editText2.removeTextChangedListener(this)
//                Format().format(binding.editText2)
//                binding.editText2.addTextChangedListener(this)
            }
        })*/
        //8
 /*       binding.editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                model.updateOperand(fraction = 8, value = s.toString())

//                if (binding.editText8.hasFocus())
//                    if (s.toString() != "" && !s!!.endsWith("."))
//                        parse(
//                            s.toString(),
//                            binding.editText8,
//                            binding.textInputLayout8,
//                            8,
//                            binding.fractionCustom.text.toString().toInt(),
//                            model.allow(8)
//                        )
//                if (s.toString() == "" && !cleared)
//                    clearConverter(binding.editText8)
//                binding.editText8.removeTextChangedListener(this)
//                Format().format(binding.editText8)
//                binding.editText8.addTextChangedListener(this)
            }
        })*/
        //16
        binding.editText16.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                model.updateOperand(fraction = 16, value = s.toString())
//                if (binding.editText16.hasFocus())
//                    if (s.toString() != "" && !s!!.endsWith(".") && binding.editText16.hasFocus())
//                        parse(
//                            s.toString(),
//                            binding.editText16,
//                            binding.textInputLayout16,
//                            16,
//                            binding.fractionCustom.text.toString().toInt(),
//                            model.allow(16)
//                        )
//                if (s.toString() == "" && !cleared)
//                    clearConverter(binding.editText16)
//                binding.editText16.removeTextChangedListener(this)
//                Format().format(binding.editText16)
//                binding.editText16.addTextChangedListener(this)
            }
        })
        //custom
        binding.editTextCustom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("test228 custom", s.toString())
                if (s != "" && s != null) {
                    model.fractionCustom.value?.let {
//                        model.updateOperand(
//                            fraction = it,
//                            value = s.toString()
//                        )
                    }
                }
//                binding.editTextCustom.hasFocus()
//                if (s.toString() != "" && !s!!.endsWith(".") && binding.editTextCustom.hasFocus())
//                    parse(
//                        s.toString(),
//                        binding.editTextCustom,
//                        binding.textInputLayoutCustom,
//                        binding.fractionCustom.text.toString().toInt(),
//                        null,
//                        model.allow(binding.fractionCustom.text.toString().toInt())
//                    )
//                if (s.toString() == "")
//                    clearConverter(binding.editTextCustom)
//                binding.editTextCustom.removeTextChangedListener(this)
//                Format().format(binding.editTextCustom)
//                binding.editTextCustom.addTextChangedListener(this)
            }
        })

        /*activityBinding.explanationFab.setOnClickListener {
            if (binding.editText10.text.toString().isNotEmpty()) {
                try {
                    val job = GlobalScope.launch(Dispatchers.Main) {
                        async {
                            bottomSheetDialog!!.show()
                            delay(10)
                            bottomSheetDialog!!.behavior.peekHeight =
                                bottomSheetInternal!!.rootView.bottomSheetCardView.height
                            delay(10)
                            bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.VISIBLE
                            delay(350)
                        }.join()
                        async {
                            bottomSheetInternal!!.rootView.bottomSheetScrollView.setPadding(
                                0,
                                bottomSheetInternal!!.rootView.bottomSheetAppBar.height - 8,
                                0,
                                0
                            )
                            bottomSheetInternal!!.rootView.bottomSheetContent.setPadding(
                                0,
                                16,
                                0,
                                0
                            )
                            parseRepeat()
                        }.join()
                        async {
                            delay(1)
                            if (bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString()
                                    .toInt() != bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString()
                                    .toInt()
                            ) {
                                bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                            }
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE


                        }
                    }
                } catch (e: Exception) {
                }
            }
        }*/
    }


    fun clearConverter(editField: View?) {
        if (!cleared) {
            cleared = true
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText10") binding.editText10.setText(
                ""
            )
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText2") binding.editText2.setText(
                ""
            )
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText8") binding.editText8.setText(
                ""
            )
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText16") binding.editText16.setText(
                ""
            )
            if (editField?.resources?.getResourceEntryName(editField.id) != "editTextCustom") binding.editTextCustom.setText(
                ""
            )
        }
        errorNull()
        activityBinding.clearFab.hide()
        activityBinding.explanationFab.hide()
    }

    private fun errorNull() {
        binding.textInputLayout10.error = null
        binding.textInputLayout2.error = null
        binding.textInputLayout8.error = null
        binding.textInputLayout16.error = null
        binding.textInputLayoutCustom.error = null
    }

    private fun checkClear() {
        if (binding.editText10.text.toString() != "" || binding.editText2.text.toString() != "" || binding.editText8.text.toString() != "" || binding.editText16.text.toString() != "" || binding.editTextCustom.text.toString() != "") {
            activityBinding.clearFab.show()
            if (binding.editText10.text.toString() != "1") {
                activityBinding.explanationFab.show()
            } else {
                activityBinding.explanationFab.show()
            }
        } else {
            activityBinding.clearFab.hide()
            activityBinding.explanationFab.hide()
            binding.editText10.setText("")
            binding.editText2.setText("")
            binding.editText8.setText("")
            binding.editText16.setText("")
            binding.editTextCustom.setText("")
        }
    }


    override fun onStart() {
        super.onStart()
//        model.load()
    }

    override fun onPause() {
        super.onPause()
//        model.save()
    }
}
