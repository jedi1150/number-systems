package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.div_layout.view.*
import kotlinx.android.synthetic.main.explanation_sheet.view.*
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.fragment_converter.view.*
import kotlinx.android.synthetic.main.multiply_layout.view.*
import kotlinx.android.synthetic.main.multiply_layout2.view.*
import kotlinx.android.synthetic.main.result_reverse.view.*


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

        view.rootView.explanation_include.visibility = View.VISIBLE
        val bottomS = BottomSheetBehavior.from(view.rootView.cardViewSheet)
        bottomS.isFitToContents = true
        bottomS.state = BottomSheetBehavior.STATE_HIDDEN
        bottomS.peekHeight = 2000

        bottomS.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, p1: Int) {
                if (bottomS.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.rootView.main_overlay.animate().alpha(1F).duration = 150
                    view.rootView.main_overlay.isClickable = true
                }
                if (bottomS.state == BottomSheetBehavior.STATE_EXPANDED) {
                    view.rootView.main_overlay.animate().alpha(1F).duration = 150
                    view.rootView.main_overlay.isClickable = true
                }
                if (bottomS.state == BottomSheetBehavior.STATE_HIDDEN) {
                    view.rootView.main_overlay.animate().alpha(0F).duration = 150
//                    if (editText10.text.toString() != "") view.rootView.converterFab?.show()
                    view.rootView.main_overlay.isClickable = false
                    view.rootView.editText10.isEnabled = true
                    view.rootView.editText2.isEnabled = true
                    view.rootView.editText8.isEnabled = true
                    view.rootView.editText16.isEnabled = true
                    view.rootView.editTextCustom.isEnabled = true
                    view.rootView.main_overlay.visibility = View.GONE
                }
                if (bottomS.state != BottomSheetBehavior.STATE_HIDDEN) {
//                    (view.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
                    view.rootView.editText10.isEnabled = false
                    view.rootView.editText2.isEnabled = false
                    view.rootView.editText8.isEnabled = false
                    view.rootView.editText16.isEnabled = false
                    view.rootView.editTextCustom.isEnabled = false
                    view.rootView.main_overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(p0: View, p1: Float) {
                view.rootView.main_overlay.alpha = (p1+1)
//                if ( > 0) {
//                    view.rootView.cardViewSheet.radius = p1*100
//                }
            }
        })

        val listCustomBin = arrayOf(3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listCustomBin)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerValue.adapter = aa

        spinnerValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                try {
                    editTextCustom.setText(editText10.text.toString().toULong(10).toString(listCustomBin[position]).toUpperCase())
                    view!!.rootView.converterFab?.show()
                    errorNull()
                    cleared = false
                }
                catch (e: Exception){
                if (editTextCustom.text.toString() != "")
                    textInputLayoutCustom.error = getString(R.string.invalid_value)
                }
            }
        }

        val converterSave = context!!.getSharedPreferences("converter", Context.MODE_PRIVATE)
        editText10.setText(converterSave.getString("converter10", ""))
        editText2.setText(converterSave.getString("converter2", ""))
        editText8.setText(converterSave.getString("converter8", ""))
        editText16.setText(converterSave.getString("converter16", ""))
        editTextCustom.setText(converterSave.getString("converterCustom", ""))
        spinnerValue.setSelection(aa.getPosition(converterSave.getInt("converterSpinner", 3)))

        val listCustomBin2 = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
        val aa2 = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listCustomBin2)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.rootView.spinner4.adapter = aa2
        view.rootView.spinner5.adapter = aa2
        view.rootView.spinner4.setSelection(aa2.getPosition(10))
        view.rootView.spinner5.setSelection(aa2.getPosition(2))

        //10
        view.rootView.editText10.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.rootView.editText10.hasFocus()) {
                    if (s.toString() != "")
                        parse(s.toString(), view.rootView.editText10, view.rootView.textInputLayout10, 10, view.rootView.spinnerValue.selectedItem.toString().toInt())
                    if (s.toString() == "" && !cleared)
                        clearConverter(view.rootView.editText10)
                }
                if (s.toString() == "0")
                {
                    view.rootView.editText10.text = null
                }
            }
        })

        //2
        view.rootView.editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.rootView.editText2.hasFocus())
                    if (s.toString() != "")
                        parse(s.toString(), view.rootView.editText2, view.rootView.textInputLayout2, 2, view.rootView.spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(view.rootView.editText2)
                if (s.toString() == "0")
                {
                    view.rootView.editText2.text = null
                }
            }
        })

        //8
        view.rootView.editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.rootView.editText8.hasFocus())
                    if (s.toString() != "" && view.rootView.editText8.hasFocus())
                        parse(s.toString(), view.rootView.editText8, view.rootView.textInputLayout8, 8, view.rootView.spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(view.rootView.editText8)
                if (s.toString() == "0")
                {
                    view.rootView.editText8.text = null
                }
            }
        })

        //16
        view.rootView.editText16.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.rootView.editText16.hasFocus())
                    if (s.toString() != "" && view.rootView.editText16.hasFocus())
                        parse(s.toString(), view.rootView.editText16, view.rootView.textInputLayout16, 16, view.rootView.spinnerValue.selectedItem.toString().toInt())
                if (s.toString() == "" && !cleared)
                    clearConverter(view.rootView.editText16)
                if (s.toString() == "0")
                {
                    view.rootView.editText16.text = null
                }
            }
        })

        //3
        view.rootView.editTextCustom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                view.rootView.editTextCustom.hasFocus()
                if (s.toString() != "" && view.rootView.editTextCustom.hasFocus())
                    parse(s.toString(), view.rootView.editTextCustom, view.rootView.textInputLayoutCustom, view.rootView.spinnerValue.selectedItem.toString().toInt(), null)
                if (s.toString() == "" && !cleared)
                    clearConverter(view.rootView.editTextCustom)
                if (s.toString() == "0")
                {
                    view.rootView.editTextCustom.text = null
                }
            }
        })

        view.rootView.explanationFab.setOnClickListener {
            (view.rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            if (view.rootView.editText10.text.toString() != "")
                parseRepeat()
            bottomS.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        view.rootView.revertButton.setOnClickListener{
            val buf = view.rootView.spinner4.selectedItemPosition
            view.rootView.spinner4.setSelection(view.rootView.spinner5.selectedItemPosition)
            view.rootView.spinner5.setSelection(buf)
            if (view.rootView.editText10.text.toString() != "") parseRepeat()

        }

        view.rootView.spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editText10?.text.toString() != "") parseRepeat()
            }
        }

        view.rootView.spinner5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (editText10.text.toString() != "") parseRepeat()
            }
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    @ExperimentalUnsignedTypes
    fun parse(string: String, editfield: View, editlayout: TextInputLayout, bin: Int, custom: Int?) =
            try {
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText10") view!!.rootView.editText10.setText(string.toULong(bin).toString(10))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText2") view!!.rootView.editText2.setText(string.toULong(bin).toString(2))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText8") view!!.rootView.editText8.setText(string.toULong(bin).toString(8))
                if (editfield.resources.getResourceEntryName(editfield.id) != "editText16") view!!.rootView.editText16.setText(string.toULong(bin).toString(16).toUpperCase())
                if (editfield.resources.getResourceEntryName(editfield.id) != "editTextCustom") view!!.rootView.editTextCustom.setText(string.toULong(bin).toString(custom!!).toUpperCase())
                view!!.rootView.converterFab.show()
                view!!.rootView.explanationFab.show()
                errorNull()
                cleared = false
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
    private fun parseRepeat() {
        try {
            view!!.rootView.hsv.visibility = View.VISIBLE
            view!!.rootView.hsv1.visibility = View.VISIBLE
            view!!.rootView.hsv2.visibility = View.VISIBLE
            view!!.rootView.hsv3.visibility = View.VISIBLE
            view!!.rootView.step1.visibility = View.VISIBLE
            view!!.rootView.step2.visibility = View.VISIBLE
            view!!.rootView.step3.visibility = View.VISIBLE
            view!!.rootView.reverseResultView.visibility = View.VISIBLE

            errorNull()
            cleared = false

            var a: String
            var b = ""
            var i = 0

            var stringOriginal = if (view!!.rootView.spinner4.selectedItem.toString().toInt() > 10) {
                editText10.text.toString()
            } else {
                editText10.text.toString().toULong(10).toString(view!!.rootView.spinner4.selectedItem.toString().toInt())
            }
            var iLast = stringOriginal.length

            view!!.rootView.explanationCardView1.removeAllViews()
            view!!.rootView.explanationCardView2.removeAllViews()
            view!!.rootView.explanationCardView3.removeAllViews()
            view!!.rootView.reverseResultView.removeAllViews()

            var typeB = 1
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

            layoutParams.setMargins(8, 8, 32, 8)

            //if spin4 = 10 and spin5 < 10
            if (view!!.rootView.spinner4.selectedItem.toString().toInt() == 10 && view!!.rootView.spinner5.selectedItem.toString().toInt() < 10) {
                view!!.rootView.hsv1.visibility = View.GONE
                view!!.rootView.hsv2.visibility = View.GONE
                view!!.rootView.step1.visibility = View.GONE
                while (stringOriginal.toULong() >= 1.toULong()) {
                    a = (stringOriginal.toULong() % view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                    vi3.textView1.text = stringOriginal
                    vi3.textView2.text = view!!.rootView.spinner5.selectedItem.toString()
                    vi3.textView3.text = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView4.text = "-" + ((stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()) * view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView5.text = a
                    b += a

                    view!!.rootView.explanationCardView3.addView(vi3, i, layoutParams)
                    i++
                    stringOriginal = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                }
                typeB = 1
            }

            //if spin4 <= 10 and spin5 < 10
            if ((view!!.rootView.spinner4.selectedItem.toString().toInt() < 10 || view!!.rootView.spinner4.selectedItem.toString().toInt() > 10) && (view!!.rootView.spinner5.selectedItem.toString().toInt() < 10 || view!!.rootView.spinner5.selectedItem.toString().toInt() > 10)) {
                stringOriginal = view!!.rootView.editText10.text.toString().toULong(10).toString(view!!.rootView.spinner4.selectedItem.toString().toInt())
                iLast = stringOriginal.length
                while (i < stringOriginal.length) {
                    iLast--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = view!!.rootView.spinner4.selectedItem.toString()
                    vi1.explanation_multiply_textView1.text = iLast.toString()
                    vi1.explanation_multiply_textView2.text = stringOriginal.reversed()[iLast].toString()
                    val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
//                    vi2.explanation_multiply2_textView1.text = ((spinner4.selectedItem.toString().toBigInteger().pow(iLast).toString().toBigInteger() * stringOriginal.reversed()[iLast].toString().toBigInteger()).toString())
                    vi2.explanation_multiply2_textView1.text =
                            ((view!!.rootView.spinner4.selectedItem.toString().toBigInteger().pow(iLast).toString().toBigInteger() *
                                    stringOriginal.reversed()[iLast].toString().toInt(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString().toInt(10).toBigInteger()).toString())
                    if (i == stringOriginal.length - 1) {
                        b = stringOriginal.toULong(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString(10)
                        vi1.explanation_multiply_plus.text = ""
                        vi2.explanation_multiply2_plus.text = " = $b"
                    }
                    view!!.rootView.explanationCardView1.addView(vi1)
                    view!!.rootView.explanationCardView2.addView(vi2)
                    i++
                }
                stringOriginal = b
                i = 0
                b = ""

                while (stringOriginal.toULong() >= 1.toULong()) {
                    a = (stringOriginal.toULong() % view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                    vi3.textView1.text = stringOriginal
                    vi3.textView2.text = view!!.rootView.spinner5.selectedItem.toString()
                    vi3.textView3.text = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView4.text = "-" + ((stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()) * view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView5.text = a
                    b += a
                    view!!.rootView.explanationCardView3.addView(vi3, i, layoutParams)
                    i++
                    stringOriginal = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                }
                typeB = 1
            }

            //if spin4 <= 10 and spin5 = 10
            if (view!!.rootView.spinner4.selectedItem.toString().toInt() < view!!.rootView.spinner5.selectedItem.toString().toInt() && view!!.rootView.spinner4.selectedItem.toString().toInt() <= 10 && view!!.rootView.spinner5.selectedItem.toString().toInt() == 10) {
                view!!.rootView.hsv3.visibility = View.GONE
                view!!.rootView.step2.visibility = View.GONE
                view!!.rootView.step3.visibility = View.GONE
                view!!.rootView.reverseResultView.visibility = View.GONE
                while (i < stringOriginal.length) {
                    iLast--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = view!!.rootView.spinner4.selectedItem.toString()
                    vi1.explanation_multiply_textView1.text = iLast.toString()
                    vi1.explanation_multiply_textView2.text = stringOriginal.reversed()[iLast].toString()

                    val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
                    vi2.explanation_multiply2_textView1.text = ((view!!.rootView.spinner4.selectedItem.toString().toBigInteger().pow(iLast).toString().toBigInteger() * stringOriginal.reversed()[iLast].toString().toBigInteger()).toString())
                    if (i == stringOriginal.length - 1) {
                        b = stringOriginal.toULong(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString(10)
                        vi1.explanation_multiply_plus.text = ""
                        vi2.explanation_multiply2_plus.text = " = $b"
                    }
                    view!!.rootView.explanationCardView1.addView(vi1)
                    view!!.rootView.explanationCardView2.addView(vi2)
                    i++
                }
                typeB = 2
            }

            //if spin5 > 10 and spin4 = 10
            if (view!!.rootView.spinner5.selectedItem.toString().toInt() > 10 && view!!.rootView.spinner4.selectedItem.toString().toInt() == 10) {
                view!!.rootView.hsv1.visibility = View.GONE
                view!!.rootView.hsv2.visibility = View.GONE
                view!!.rootView.step1.visibility = View.GONE
                i = 0
                b = ""
                while (stringOriginal.toULong() >= 1.toULong()) {
                    a = (stringOriginal.toULong() % view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                    vi3.textView1.text = stringOriginal
                    vi3.textView2.text = view!!.rootView.spinner5.selectedItem.toString()
                    vi3.textView3.text = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView4.text = "-" + ((stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()) * view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    if (a.toInt() >= 10) {
                        vi3.textView5.text = a + "=" + a.toInt(10).toString(view!!.rootView.spinner5.selectedItem.toString().toInt()).toUpperCase()
                        b += a.toInt(10).toString(view!!.rootView.spinner5.selectedItem.toString().toInt()).toUpperCase()
                    } else {
                        vi3.textView5.text = a
                        b += a
                    }
                    view!!.rootView.explanationCardView3.addView(vi3, i, layoutParams)
                    i++
                    stringOriginal = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                }
                typeB = 1
            }

            //if spin4 > 10 and spin5 = 10
            if (view!!.rootView.spinner4.selectedItem.toString().toInt() > 10 && view!!.rootView.spinner5.selectedItem.toString().toInt() == 10) {
                view!!.rootView.hsv3.visibility = View.GONE
                view!!.rootView.step2.visibility = View.GONE
                view!!.rootView.step3.visibility = View.GONE
                view!!.rootView.reverseResultView.visibility = View.GONE

                stringOriginal = view!!.rootView.editText10.text.toString().toULong(10).toString(view!!.rootView.spinner4.selectedItem.toString().toInt())
                iLast = stringOriginal.length
                while (i < stringOriginal.length) {
                    iLast--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = view!!.rootView.spinner4.selectedItem.toString()
                    vi1.explanation_multiply_textView1.text = iLast.toString()
                    vi1.explanation_multiply_textView2.text = stringOriginal.reversed()[iLast].toString().toInt(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString().toBigInteger(10).toString()
                    val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
                    vi2.explanation_multiply2_textView1.text = ((view!!.rootView.spinner4.selectedItem.toString().toBigInteger().pow(iLast).toString().toBigInteger() * stringOriginal.reversed()[iLast].toString().toBigInteger(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString().toBigInteger(10)).toString())
                    if (i == stringOriginal.length - 1) {
                        b = stringOriginal.toULong(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString(10)
                        vi1.explanation_multiply_plus.text = ""
                        vi2.explanation_multiply2_plus.text = " = $b"
                    }
                    view!!.rootView.explanationCardView1.addView(vi1)
                    view!!.rootView.explanationCardView2.addView(vi2)
                    i++
                }
                b = b.reversed()
                typeB = 1
            }

            //if spin4 > 10 and spin5 > 10
            if ((view!!.rootView.spinner4.selectedItem.toString().toInt() > 10 && view!!.rootView.spinner5.selectedItem.toString().toInt() != 10) && (view!!.rootView.spinner5.selectedItem.toString().toInt() > 10 && view!!.rootView.spinner4.selectedItem.toString().toInt() != 10)) {
                stringOriginal = view!!.rootView.editText10.text.toString().toULong(10).toString(view!!.rootView.spinner4.selectedItem.toString().toInt())
                iLast = stringOriginal.length
                while (i < stringOriginal.length) {
                    iLast--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = view!!.rootView.spinner4.selectedItem.toString()
                    vi1.explanation_multiply_textView1.text = iLast.toString()
                    vi1.explanation_multiply_textView2.text = stringOriginal.reversed()[iLast].toString().toInt(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString().toInt(10).toString()
                    val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
                    vi2.explanation_multiply2_textView1.text =
                            ((view!!.rootView.spinner4.selectedItem.toString().toBigInteger().pow(iLast).toString().toBigInteger() *
                                    stringOriginal.reversed()[iLast].toString().toInt(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString().toInt(10).toBigInteger()).toString())
                    if (i == stringOriginal.length - 1) {
                        b = stringOriginal.toULong(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString(10)
                        vi1.explanation_multiply_plus.text = ""
                        vi2.explanation_multiply2_plus.text = " = $b"
                    }
                    view!!.rootView.explanationCardView1.addView(vi1)
                    view!!.rootView.explanationCardView2.addView(vi2)
                    i++
                }
                i = 0
                b = ""
                stringOriginal = stringOriginal.toULong(view!!.rootView.spinner4.selectedItem.toString().toInt()).toString(10)
                while (stringOriginal.toULong() >= 1.toULong()) {
                    a = (stringOriginal.toULong() % view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                    vi3.textView1.text = stringOriginal
                    vi3.textView2.text = view!!.rootView.spinner5.selectedItem.toString()
                    vi3.textView3.text = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    vi3.textView4.text = "-" + ((stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()) * view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                    if (a.toInt() >= 10) {
                        vi3.textView5.text = a + "=" + a.toInt(10).toString(view!!.rootView.spinner5.selectedItem.toString().toInt()).toUpperCase()
                        b += a.toInt(10).toString(view!!.rootView.spinner5.selectedItem.toString().toInt()).toUpperCase()
                    } else {
                        vi3.textView5.text = a
                        b += a
                    }
                    view!!.rootView.explanationCardView3.addView(vi3, i, layoutParams)
                    i++
                    stringOriginal = (stringOriginal.toULong() / view!!.rootView.spinner5.selectedItem.toString().toULong()).toString()
                }
                typeB = 1
            }
            if (view!!.rootView.spinner4.selectedItem.toString().toInt() == view!!.rootView.spinner5.selectedItem.toString().toInt())
            {
                view!!.rootView.hsv.visibility = View.GONE
                view!!.rootView.hsv1.visibility = View.GONE
                view!!.rootView.hsv2.visibility = View.GONE
                view!!.rootView.hsv3.visibility = View.GONE
                view!!.rootView.step1.visibility = View.GONE
                view!!.rootView.step2.visibility = View.GONE
                view!!.rootView.step3.visibility = View.GONE
                view!!.rootView.reverseResultView.visibility = View.GONE
            }

            view!!.rootView.explanation_number_1.text = view!!.rootView.editText10.text.toString().toULong(10).toString(view!!.rootView.spinner4.selectedItem.toString().toInt()).toUpperCase()
            view!!.rootView.convert_number_1.text = view!!.rootView.spinner4.selectedItem.toString()
            if (typeB == 1) view!!.rootView.explanation_number_2.text = b.reversed()
            if (typeB == 2) view!!.rootView.explanation_number_2.text = b.toULong(10).toString(view!!.rootView.spinner5.selectedItem.toString().toInt()).toUpperCase()
            view!!.rootView.convert_number_2.text = view!!.rootView.spinner5.selectedItem.toString()
//            textReverseResult.text = "$b -> ${b.reversed()}"
            val viResult = layoutInflater.inflate(R.layout.result_reverse, null, false)
            viResult.textResultExplanation1.text = b
            viResult.textResultExplanation2.text = b.reversed()
            viResult.resultConvertNumber1.text = view!!.rootView.spinner5.selectedItem.toString()
            viResult.resultConvertNumber2.text = view!!.rootView.spinner5.selectedItem.toString()
            view!!.rootView.reverseResultView.addView(viResult)
        }
        catch (e: Exception) {
        }
    }

    private fun clearConverter(editfield: View?)
    {
        if (!cleared) {
            cleared = true
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText10") editText10?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText2") editText2?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText8") editText8?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editText16") editText16?.setText("")
            if (editfield?.resources?.getResourceEntryName(editfield.id) != "editTextCustom") editTextCustom?.setText("")
            view!!.rootView.explanationCardView1.removeAllViews()
        }
        errorNull()
        view!!.rootView.converterFab.hide()
        view!!.rootView.explanationFab.hide()
    }

    private fun errorNull()
    {
        textInputLayout10?.error = null
        textInputLayout2?.error = null
        textInputLayout8?.error = null
        textInputLayout16?.error = null
        textInputLayoutCustom?.error = null
    }

    override fun onResume() {
        super.onResume()
        if (editText10.text.toString() != "" && view!!.rootView.tabs.getTabAt(0)!!.isSelected) {
//            view!!.rootView.explanationFab.show()
//            view!!.rootView.converterFab.show()
//            view!!.rootView.calculatorFab.hide()
            view!!.rootView.tabs.getTabAt(0)!!.select()
        }

            val bottomS = BottomSheetBehavior.from(view!!.rootView.cardViewSheet)
        if (bottomS.state != BottomSheetBehavior.STATE_HIDDEN) {
            view!!.rootView.main_overlay.alpha = 1f
            view!!.rootView.main_overlay.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = context!!.getSharedPreferences("converter", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("converter10", editText10.text.toString()).apply()
        editor.putString("converter2", editText2.text.toString()).apply()
        editor.putString("converter8", editText8.text.toString()).apply()
        editor.putString("converter16", editText16.text.toString()).apply()
        editor.putString("converterCustom", editTextCustom.text.toString()).apply()
        editor.putInt("converterSpinner", spinnerValue.selectedItem.toString().toInt()).apply()
    }
}
