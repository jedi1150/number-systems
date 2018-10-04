package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.decision_sheet.*
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.div_layout.view.*


@Suppress("DEPRECATION")
class ConverterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalUnsignedTypes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rootView.converterFab.setOnClickListener {
            clearConverter(it)
        }

        decision_include.visibility = View.VISIBLE
        val bottomS = BottomSheetBehavior.from(sheet)
        bottomS.isFitToContents = true
        bottomS.state = BottomSheetBehavior.STATE_HIDDEN
        bottomS.peekHeight = 600

        bottomS.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                if (bottomS.state == BottomSheetBehavior.STATE_COLLAPSED) {
//                    main_overlay.animate().alpha(1F).duration = 150
//                    main_overlay.isClickable = true
                }
                if (bottomS.state == BottomSheetBehavior.STATE_EXPANDED) {
//                    main_overlay.animate().alpha(1F).duration = 150
//                    main_overlay.isClickable = true
                }
                if (bottomS.state == BottomSheetBehavior.STATE_HIDDEN) {
//                    main_overlay.animate().alpha(0F).duration = 150
//                    main_overlay.isClickable = false
                }
            }

            override fun onSlide(p0: View, p1: Float) {
//                main_overlay.alpha = (1+p1)
            }
        })

        val listCustomBin = arrayOf(3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listCustomBin)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerValue!!.adapter = aa

        spinnerValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                try {
                    if(!editTextCustom.hasFocus())
                        editTextCustom.setText(editText10.text.toString().toULong(10).toString(listCustomBin[position]).toUpperCase())
                    else {
                        editText10.setText(editTextCustom.text.toString().toULong(listCustomBin[position]).toString(10))
                        editText2.setText(editTextCustom.text.toString().toULong(listCustomBin[position]).toString(2))
                        editText8.setText(editTextCustom.text.toString().toULong(listCustomBin[position]).toString(8))
                        editText16.setText(editTextCustom.text.toString().toULong(listCustomBin[position]).toString(16).toUpperCase())
                    }
                    view?.rootView?.converterFab?.show()
                    errorNull()
                    cleared = false
                }
                catch (e: Exception){
                if (editTextCustom.text.toString() != "")
                    textInputLayoutCustom.error = getString(R.string.invalid_value)
                }
            }
        }

        val listCustomBin2 = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
        val aa2 = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listCustomBin2)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner4.adapter = aa2
        spinner5.adapter = aa2
        spinner4.setSelection(aa2.getPosition(10))
        spinner5.setSelection(aa2.getPosition(2))

        //10
        editText10.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText10.hasFocus()) {
                    if (s.toString() != "")
                        parse(s.toString(), editText10, textInputLayout10, 10, spinnerValue.selectedItem.toString().toInt())
                    if (s.toString() == "" && !cleared)
                        clearConverter(editText10)
                }
            }
        })

        //2
        editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText2.hasFocus())
                    if (s.toString() != "")
                        parse(s.toString(), editText2, textInputLayout2, 2, spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(editText2)
            }
        })

        //8
        editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText8.hasFocus())
                    if (s!!.toString() != "" && editText8.hasFocus())
                        parse(s.toString(), editText8, textInputLayout8, 8, spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(editText8)
            }
        })

        //16
        editText16.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText16.hasFocus())
                    if (s!!.toString() != "" && editText16.hasFocus())
                        parse(s.toString(), editText16, textInputLayout16, 16, spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(editText16)
            }
        })

        //3
        editTextCustom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextCustom.hasFocus()
                if (s!!.toString() != "" && editTextCustom.hasFocus())
                    parse(s.toString(), editTextCustom, textInputLayoutCustom, spinnerValue.selectedItem.toString().toInt(), null)
                if (s.toString() == "" && !cleared)
                    clearConverter(editTextCustom)
            }
        })

        decision_button.setOnClickListener {
            bottomS.state = BottomSheetBehavior.STATE_COLLAPSED
            if (editText10.text.toString() != "") parseRepeat()
        }

        revertButton.setOnClickListener{
            val buf = spinner4.selectedItemPosition
            spinner4.setSelection(spinner5.selectedItemPosition)
            spinner5.setSelection(buf)
            if (editText10.text.toString() != "") parseRepeat()
        }

        spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tv1.text = spinner4.selectedItem.toString() + " -> " + spinner5.selectedItem.toString()
                if (editText10.text.toString() != "") parseRepeat()
            }
        }

        spinner5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tv1.text = spinner4.selectedItem.toString() + " -> " + spinner5.selectedItem.toString()
                if (editText10.text.toString() != "") parseRepeat()
            }
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    @ExperimentalUnsignedTypes
    fun parse(string: String, editfield: View, editlayout: TextInputLayout, bin: Int, custom: Int?) =
            try {
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText10") editText10.setText(string.toULong(bin).toString(10))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText2") editText2.setText(string.toULong(bin).toString(2))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText8") editText8.setText(string.toULong(bin).toString(8))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText16") editText16.setText(string.toULong(bin).toString(16).toUpperCase())
                if (editfield.resources.getResourceEntryName(editfield.id) != "editTextCustom") editTextCustom.setText(string.toULong(bin).toString(custom!!).toUpperCase())
                view?.rootView?.converterFab?.show()
                errorNull()
                cleared = false
                decision_button.isEnabled = true
            }
            catch (e: Exception){
                Log.d("test228", e.toString())
                val editText: EditText = editfield as EditText
                editText.setText(editText.text.toString().substring(0, editText.text.toString().length - 1))
                editText.setSelection(editText.text.toString().length)
                editlayout.error = getString(ru.sandello.binaryconverter.R.string.limitIsExceeded)
            }

    @ExperimentalUnsignedTypes
    @SuppressLint("SetTextI18n", "InflateParams")
    fun parseRepeat() =
            try {
                errorNull()
                cleared = false

                var a: String
                var i = 0
                var stringOriginal = editText10.text.toString().toULong(10).toString(spinner4.selectedItem.toString().toInt()).toInt()

                decisionCardView1.removeAllViews()

                if (spinner4.selectedItem.toString().toInt() > spinner5.selectedItem.toString().toInt())
                    while (stringOriginal >= 1)
                    {
                        a = (stringOriginal % spinner5.selectedItem.toString().toInt()).toString()
                        val vi = layoutInflater.inflate(R.layout.div_layout, null, false)
                        vi.textView1.text = stringOriginal.toString()
                        vi.textView2.text = spinner5.selectedItem.toString()
                        vi.textView3.text = (stringOriginal / spinner5.selectedItem.toString().toInt()).toString()
                        vi.textView4.text = "-" + ((stringOriginal / spinner5.selectedItem.toString().toInt()) * spinner5.selectedItem.toString().toInt()).toString()
                        vi.textView5.text = a

                        decisionCardView1.addView(vi, i)
                        vi.id = i
                        i++
                        stringOriginal /= spinner5.selectedItem.toString().toInt()
                    }
                if (spinner4.selectedItem.toString().toInt() < spinner5.selectedItem.toString().toInt())
                {
                    while (i < stringOriginal.toString().length) {
//                        Log.d("test228", stringOriginal.toString().subSequence(0, stringOriginal.toString().length)[i].toString())
                        i++
                    }
                    val tv = TextView(context)
                    tv.text = stringOriginal.toString() + spinner4.selectedItem.toString().substring(0) + " " +
                            "-> " + stringOriginal.toString().toInt(spinner4.selectedItem.toString().toInt()).toString(10) + Html.fromHtml("<sup><small>${spinner5.selectedItem}</small></sup> ")
                    decisionCardView1.addView(tv)
                }
                else
                {

                }

            }
            catch (e: Exception){
                Log.d("test228", e.toString())
            }

    private fun clearConverter(editfield: View?)
    {
        if (!cleared) {
            cleared = true
            decision_button.isEnabled = false
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText10") editText10?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText2") editText2?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText8") editText8?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText16") editText16?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editTextCustom") editTextCustom?.setText("")
            decisionCardView1.removeAllViews()
        }
        errorNull()
        view?.rootView?.converterFab?.hide()
    }

    private fun errorNull()
    {
        textInputLayout10?.error = null
        textInputLayout2?.error = null
        textInputLayout8?.error = null
        textInputLayout16?.error = null
        textInputLayoutCustom?.error = null
    }
}
