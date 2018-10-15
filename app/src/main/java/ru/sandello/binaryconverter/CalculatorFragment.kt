package ru.sandello.binaryconverter
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.fragment_calculator.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@ExperimentalUnsignedTypes
class CalculatorFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var a = 0.toULong()
    private var b = 0.toULong()
    private var c = 0.toULong()

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

        val calculatorSave = context!!.getSharedPreferences("calculator", Context.MODE_PRIVATE)
        editTextVal1.setText(calculatorSave.getString("calculator1", ""))
        editTextVal2.setText(calculatorSave.getString("calculator2", ""))
        radioGroup.check(calculatorSave.getInt("switch", radioButtonPlus.id))
        spinner.setSelection(aa.getPosition(calculatorSave.getInt("spinner1", 10)))
        spinner2.setSelection(aa.getPosition(calculatorSave.getInt("spinner2", 2)))
        spinner3.setSelection(aa.getPosition(calculatorSave.getInt("spinner3", 10)))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editTextVal1.text.toString() != "")
                try {
                    a = editTextVal1.text.toString().toULong(spinner.selectedItem.toString().toInt())
                    if (radioButtonPlus.isChecked) {
                        c = (a + b)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a - b)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a * b)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toULong()) {
                        c = (a / b)
                    }
                    editTextResult.setText(c.toString(spinner3.selectedItem.toString().toInt()))
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
                    b = editTextVal2.text.toString().toULong(spinner2.selectedItem.toString().toInt())
                    if (radioButtonPlus.isChecked) {
                        c = (a + b)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a - b)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a * b)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toULong()) {
                        c = (a / b)
                    }
                    editTextResult.setText(c.toString(spinner3.selectedItem.toString().toInt()))
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
                        c = (a + b)
                    }
                    if (radioButtonMinus.isChecked) {
                        c = (a - b)
                    }
                    if (radioButtonMult.isChecked) {
                        c = (a * b)
                    }
                    if (radioButtonDiv.isChecked && b != 0.toULong()) {
                        c = (a / b)
                    }
                    editTextResult.setText(c.toString(spinner3.selectedItem.toString().toInt()))
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
                        calculate(s.toString(), editTextVal1, textInputLayoutCustom1)
                    }
                    if (s.toString() == "" && !clearedCalc)
                        a = 0.toULong(); calculate("", editTextVal2, textInputLayoutCustom2)
                    if (s.toString() == "0")
                    {
                        view.rootView.editTextVal1.text = null
                    }
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
                        calculate(s.toString(), editTextVal2, textInputLayoutCustom2)
                    }
                    if (s.toString() == "" && !clearedCalc)
                        b = 0.toULong(); calculate("", editTextVal2, textInputLayoutCustom2)
                    if (s.toString() == "0")
                    {
                        view.rootView.editTextVal1.text = null
                    }
                }
                if (editTextVal1.text.toString() == "" && editTextVal2.text.toString() == "") view.rootView?.calculatorFab?.hide()
            }
        })

        radioGroup.setOnCheckedChangeListener { _, _ ->
            calculateRadio()
        }
    }

    fun calculate(string: String, editField: View?, editLayout: TextInputLayout?)
    {
        try {
            if (editField?.resources?.getResourceEntryName(editField.id) == "editTextVal1" && string != "") a = string.toULong(spinner.selectedItem.toString().toInt())
            if (editField?.resources?.getResourceEntryName(editField.id) == "editTextVal2" && string != "") b = string.toULong(spinner2.selectedItem.toString().toInt())
                if (radioButtonPlus.isChecked) {
                    c = (a + b)
                }
                if (radioButtonMinus.isChecked) {
                    c = (a - b)
                }
                if (radioButtonMult.isChecked) {
                    c = (a * b)
                }
                if (radioButtonDiv.isChecked && b != 0.toULong()) {
                    c = (a / b)
                }
            editTextResult.setText(c.toString(spinner3.selectedItem.toString().toInt()))
            view?.rootView?.calculatorFab?.show()
            clearedCalc = false
            errorNull()
        }
        catch (e: Exception){
            val editText: EditText = editField as EditText
            editText.setText(editText.text.toString().substring(0, editText.text.toString().length - 1))
            editText.setSelection(editText.text.toString().length)
            editLayout?.error = getString(ru.sandello.binaryconverter.R.string.limitIsExceeded)
        }
    }

    private fun calculateRadio()
    {
        try {
            if (radioButtonPlus.isChecked) {
                c = (a + b)
            }
            if (radioButtonMinus.isChecked) {
                c = (a - b)
            }
            if (radioButtonMult.isChecked) {
                c = (a * b)
            }
            if (radioButtonDiv.isChecked && b != 0.toULong()) {
                c = (a / b)
            }
            editTextResult.setText(c.toString())
            errorNull()
        }
        catch (e: Exception){ }
    }

    private fun clearCalculator(editField: View?)
    {
        if (!clearedCalc) {
            clearedCalc = true
            if (editField?.resources?.getResourceEntryName(editField.id) != "editTextVal1") editTextVal1.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editTextVal2") editTextVal2.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editTextResult") editTextResult.setText("0")
            a = 0.toULong()
            b = 0.toULong()
            c = 0.toULong()
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

    override fun onResume() {
        super.onResume()
        if (editTextVal1.text.toString() != "" && view!!.rootView.tabs.getTabAt(1)!!.isSelected) {
//            view!!.rootView.explanationFab.hide()
//            view!!.rootView.converterFab.hide()
//            view!!.rootView.calculatorFab.show()
//            view!!.rootView.tabs.getTabAt(1)!!.select()
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = context!!.getSharedPreferences("calculator", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("calculator1", editTextVal1.text.toString()).apply()
        editor.putString("calculator2", editTextVal2.text.toString()).apply()
        editor.putInt("switch", radioGroup.checkedRadioButtonId).apply()
        editor.putInt("spinner1", spinner.selectedItem.toString().toInt()).apply()
        editor.putInt("spinner2", spinner2.selectedItem.toString().toInt()).apply()
        editor.putInt("spinner3", spinner3.selectedItem.toString().toInt()).apply()
    }
}
