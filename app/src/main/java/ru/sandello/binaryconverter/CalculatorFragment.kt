package ru.sandello.binaryconverter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.fragment_calculator.view.*
import java.math.RoundingMode

class CalculatorFragment : Fragment() {
    private var a = 0.toBigDecimal()
    private var b = 0.toBigDecimal()
    private var c = 0.toBigDecimal()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    private val listCustomBin = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
    private val sym = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load()
        myClipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        editTextResult.setOnClickListener { "calResult".copyVal() }

        view.rootView.clear_fab.setOnClickListener {
            editTextVal1.setText("")
            editTextVal2.setText("")
            editTextResult.setText("")
            it.clear_fab.hide()
        }

        fun allow(to: EditText): String {
            var allowVal = ""
            for (i in 0 until to.text.toString().toInt())
                allowVal += sym[i]
            return allowVal
        }

        spinner.setOnItemClickListener { _, _, _, _ ->
            TypeMethod().type(editTextVal1, allow(spinner))
            calculate(allow(spinner))
        }

        spinner2.setOnItemClickListener { _, _, _, _ ->
            TypeMethod().type(editTextVal2, allow(spinner2))
            calculate(allow(spinner2))
        }

        spinner3.setOnItemClickListener { _, _, _, _ ->
            calculate("")
        }

        editTextVal1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                view.rootView!!.editTextVal1.removeTextChangedListener(this)
                Format().format(editTextVal1)
                view.rootView!!.editTextVal1.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TypeMethod().type(editTextVal1, allow(spinner))
                calculate(allow(spinner))
            }
        })


        editTextVal2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                view.rootView!!.editTextVal2.removeTextChangedListener(this)
                Format().format(editTextVal2)
                view.rootView!!.editTextVal2.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TypeMethod().type(editTextVal2, allow(spinner2))
                calculate(allow(spinner2))
            }
        })

        toggleGroup.addOnButtonCheckedListener { _, _, _ ->
            calculate("")
        }
    }


    fun calculate(allowVal: String) {
        checkClear()
        errorNull()
        if (editTextVal1.text.toString().isNotEmpty())
            try {
                a = ConvertTo().main(editTextVal1.text.toString(), spinner.text.toString().toInt(), 10).toBigDecimal()
            } catch (e: Exception) {
                textInputLayoutCustom1?.error = getString(R.string.available_characters_for_input) + ": $allowVal"
                e.printStackTrace()
            }
        if (editTextVal2.text.toString().isNotEmpty())
            try {
                b = ConvertTo().main(editTextVal2.text.toString(), spinner2.text.toString().toInt(), 10).toBigDecimal()
            } catch (e: Exception) {
                textInputLayoutCustom2?.error = getString(R.string.available_characters_for_input) + ": $allowVal"
                e.printStackTrace()
            }
        if (textInputLayoutCustom1.error == null && textInputLayoutCustom2.error == null) {
            if (togglePlus.isChecked) c = (a + b)
            if (toggleMinus.isChecked) c = (a - b)
            if (toggleMult.isChecked) c = (a * b)
            if (toggleDiv.isChecked && b != 0.toBigDecimal()) c = a.divide(b, fractionCount, RoundingMode.HALF_UP)
            if (editTextVal1.text.toString().isNotEmpty() && editTextVal2.text.toString().isNotEmpty() && !(editTextVal2.text.toString() == "0" && toggleDiv.isChecked))
                editTextResult.setText(ConvertTo().main(c.toString(), 10, spinner3.text.toString().toInt()))
            else
                editTextResult.setText("")
        } else {
            editTextResult.setText("")
        }
        save()
    }

    private fun errorNull() {
        view!!.rootView.textInputLayoutCustom1.error = null
        view!!.rootView.textInputLayoutCustom2.error = null
        view!!.rootView.textInputLayoutResult.error = null
    }

    private fun checkClear() {
        if (editTextVal1.text.toString() != "" || editTextVal2.text.toString() != "")
            view!!.rootView.clear_fab.show()
        else
            view!!.rootView.clear_fab.hide()
    }

    private fun String.copyVal() {
        if (this == "calResult" && editTextResult.text.toString() != "0" && editTextResult.text.toString() != "") {
            myClip = ClipData.newPlainText("text", editTextResult.text.toString())
            myClipboard!!.setPrimaryClip(myClip!!)
            Snackbar.make(view!!.rootView.snackbar, "${getString(R.string.copied)}: ${editTextResult.text}", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun load() {
        val calculatorSave = context!!.getSharedPreferences("calculator", Context.MODE_PRIVATE)
        editTextVal1.setText(calculatorSave.getString("calculator1", ""))
        editTextVal2.setText(calculatorSave.getString("calculator2", ""))
        toggleGroup.check(calculatorSave.getInt("toggle", togglePlus.id))
        val adapter = ArrayAdapter(
                context!!,
                R.layout.dropdown_menu_popup_item,
                listCustomBin)
        spinner.setText(calculatorSave.getInt("spinner1", 10).toString())
        spinner2.setText(calculatorSave.getInt("spinner2", 2).toString())
        spinner3.setText(calculatorSave.getInt("spinner3", 10).toString())
        spinner.setAdapter(adapter)
        spinner2.setAdapter(adapter)
        spinner3.setAdapter(adapter)
        try {
            if (editTextVal1.text.toString() != "")
                a = ConvertTo().main(editTextVal1.text.toString(), spinner.text.toString().toInt(), 10).toBigDecimal()
            if (editTextVal2.text.toString() != "")
                b = ConvertTo().main(editTextVal2.text.toString(), spinner2.text.toString().toInt(), 2).toBigDecimal()
        } catch (e: Exception) {
        }
        calculate("")
    }

    private fun save() {
        val sharedPref = context!!.getSharedPreferences("calculator", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("calculator1", editTextVal1.text.toString()).apply()
        editor.putString("calculator2", editTextVal2.text.toString()).apply()
        editor.putInt("toggle", toggleGroup.checkedButtonId).apply()
        editor.putInt("spinner1", spinner.text.toString().toInt()).apply()
        editor.putInt("spinner2", spinner2.text.toString().toInt()).apply()
        editor.putInt("spinner3", spinner3.text.toString().toInt()).apply()
    }

    override fun onStart() {
        super.onStart()
        load()
    }

    override fun onPause() {
        super.onPause()
        save()
    }
}
