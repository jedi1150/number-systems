package ru.sandello.binaryconverter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_calculator.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@ExperimentalUnsignedTypes
class CalculatorFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var a: Long? = 0.toLong()
    private var b: Long? = 0.toLong()
    private var c: Long? = 0.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listCustomBin = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listCustomBin)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa
        spinner2!!.adapter = aa
        spinner3!!.adapter = aa

        spinner.setSelection(aa.getPosition(10))
        spinner2.setSelection(aa.getPosition(2))
        spinner3.setSelection(aa.getPosition(10))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editTextVal1.text.toString() != "")
                try {
                    a = editTextVal1.text.toString().toLong(spinner.selectedItem.toString().toInt())
                    if (radioButtonPlus.isChecked) {
                        c = (a!! + b!!)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a!! - b!!)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a!! * b!!)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toLong()) {
                        c = (a!! / b!!)
                    }
                    editTextResult.setText(c?.toString(spinner3.selectedItem.toString().toInt()))
                    view?.rootView?.calculatorFab?.show()
                    clearedCalc = false
                    errorNull()
                }
                catch (e: Exception){
                    textInputLayoutCustom1.error = getString(R.string.invalid_value)
                }
            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editTextVal2.text.toString() != "")
                try {
                    b = editTextVal2.text.toString().toLong(spinner2.selectedItem.toString().toInt())
                    if (radioButtonPlus.isChecked) {
                        c = (a!! + b!!)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a!! - b!!)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a!! * b!!)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toLong()) {
                        c = (a!! / b!!)
                    }
                    editTextResult.setText(c?.toString(spinner3.selectedItem.toString().toInt()))
                    view?.rootView?.calculatorFab?.show()
                    clearedCalc = false
                    errorNull()
                }
                catch (e: Exception){
                    if (editTextVal2.text.toString() != "")
                    textInputLayoutCustom2.error = getString(ru.sandello.binaryconverter.R.string.invalid_value)
                }
            }
        }

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editTextResult.text.toString() != "")
                try {
                    if (radioButtonPlus.isChecked) {
                        c = (a!! + b!!)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a!! - b!!)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a!! * b!!)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toLong()) {
                        c = (a!! / b!!)
                    }
                    editTextResult.setText(c?.toString(spinner3.selectedItem.toString().toInt()))
                    view?.rootView?.calculatorFab?.show()
                    clearedCalc = false
                    errorNull()
                }
                catch (e: Exception){
                    textInputLayoutResult.error = getString(ru.sandello.binaryconverter.R.string.invalid_value)
                }
            }
        }


        view.rootView.calculatorFab.setOnClickListener {
            clearCalculator(it)
        }

        editTextVal1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editTextVal1.hasFocus()) {
                    if (s.toString() != "")
                    {
                        calculate(s.toString(), editTextVal1, textInputLayoutCustom1, spinner.selectedItem.toString().toInt())
                    }
                    if (s.toString() == "" && !clearedCalc)
                        a = 0.toLong(); calculate("", editTextVal2, textInputLayoutCustom2, spinner2.selectedItem.toString().toInt())
                }
                if (editTextVal1.text.toString() == "" && editTextVal2.text.toString() == "") view.rootView?.converterFab?.hide()
            }
        })

        editTextVal2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editTextVal2.hasFocus()) {
                    if (s.toString() != "") {
                        calculate(s.toString(), editTextVal2, textInputLayoutCustom2, spinner2.selectedItem.toString().toInt())
                    }
                    if (s.toString() == "" && !clearedCalc)
                        b = 0.toLong(); calculate("", editTextVal2, textInputLayoutCustom2, spinner2.selectedItem.toString().toInt())
                }
                if (editTextVal1.text.toString() == "" && editTextVal2.text.toString() == "") view.rootView?.calculatorFab?.hide()
            }
        })

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            calculateRadio()
        }
    }

    fun calculate(string: String, editfield: View?, editlayout: TextInputLayout?, bin: Int)
    {
        try {
            if (editfield?.resources?.getResourceEntryName(editfield.id) == "editTextVal1" && string != "") a = string.toLong(spinner.selectedItem.toString().toInt())
            if (editfield?.resources?.getResourceEntryName(editfield.id) == "editTextVal2" && string != "") b = string.toLong(spinner2.selectedItem.toString().toInt())
                if (radioButtonPlus.isChecked) {
                    c = (a!! + b!!)
                }
                if (radioButtonMinus.isChecked) {
                    c = (a!! - b!!)
                }
                if (radioButtonMult.isChecked) {
                    c = (a!! * b!!)
                }
                if (radioButtonDiv.isChecked && b != 0.toLong()) {
                    c = (a!! / b!!)
                }
            editTextResult.setText(c?.toString(spinner3.selectedItem.toString().toInt()))
            view?.rootView?.calculatorFab?.show()
            clearedCalc = false
            errorNull()
        }
        catch (e: Exception){
            val edittext: EditText = editfield as EditText
            edittext.setText(edittext.text.toString().substring(0, edittext.text.toString().length - 1))
            edittext.setSelection(edittext.text.toString().length)
            editlayout?.error = getString(ru.sandello.binaryconverter.R.string.limitIsExceeded)
        }
    }

    fun calculateRadio()
    {
        try {
            if (radioButtonPlus.isChecked) {
                c = (a!! + b!!)
            }
            if (radioButtonMinus.isChecked) {
                c = (a!! - b!!)
            }
            if (radioButtonMult.isChecked) {
                c = (a!! * b!!)
            }
            if (radioButtonDiv.isChecked && b != 0.toLong()) {
                c = (a!! / b!!)
            }
            editTextResult.setText(c.toString())
            errorNull()
        }
        catch (e: Exception){ }
    }

    private fun clearCalculator(editfield: View?)
    {
        if (!clearedCalc) {
            clearedCalc = true
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editTextVal1") editTextVal1.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editTextVal2") editTextVal2.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editTextResult") editTextResult.setText("0")
            a = 0.toLong()
            b = 0.toLong()
            c = 0.toLong()
        }
        errorNull()
        view?.rootView?.calculatorFab?.hide()
    }

    private fun errorNull()
    {
        textInputLayoutCustom1.error = null
        textInputLayoutCustom2.error = null
        textInputLayoutResult.error = null
    }
}
