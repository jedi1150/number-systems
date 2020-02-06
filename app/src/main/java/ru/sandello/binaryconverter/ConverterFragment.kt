package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.answer_layout.view.*
import kotlinx.android.synthetic.main.div_layout.view.*
import kotlinx.android.synthetic.main.frag_layout.view.*
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.fragment_converter.view.*
import kotlinx.android.synthetic.main.fragment_explanation.view.*
import kotlinx.android.synthetic.main.multiply_layout.view.*
import kotlinx.android.synthetic.main.multiply_layout2.view.*
import kotlinx.android.synthetic.main.multiply_layout3.view.*
import kotlinx.android.synthetic.main.multiply_layout4.view.*
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow


var cleared = false

@ExperimentalUnsignedTypes
class ConverterFragment : Fragment() {
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetInternal: View? = null
    private val listCustomBin = arrayOf(3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)
    private val listAllBin = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    @SuppressLint("SetTextI18n", "RestrictedApi")
    @ExperimentalUnsignedTypes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog!!.setContentView(R.layout.fragment_explanation)
        bottomSheetInternal = bottomSheetDialog!!.findViewById(R.id.cardViewSheet)
        bottomSheetInternal!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        bottomSheetDialog!!.behavior.skipCollapsed = true
        bottomSheetDialog!!.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) {
                    bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                    bottomSheetDialog!!.dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        view.rootView.clear_fab.setOnClickListener {
            editText10?.setText("")
            editText2?.setText("")
            editText8?.setText("")
            editText16?.setText("")
            editTextCustom?.setText("")
            it.clear_fab.hide()
            view.rootView.explanation_fab.hide()
        }

        filled_exposed_dropdown.setOnItemClickListener { _, _, position, _ ->
            try {
                editTextCustom.setText(ConvertTo().main(editText10.text.toString(), 10, listCustomBin[position]))
                errorNull()
            } catch (e: Exception) {
                if (editTextCustom?.text.toString() != "")
                    textInputLayoutCustom?.error = getString(R.string.invalid_value)
            }
        }

        val spinnerSharedPref = context!!.getSharedPreferences("spinner", Context.MODE_PRIVATE)
        val spinnerEditor = spinnerSharedPref.edit()

        val adapter = ArrayAdapter(
                context!!,
                R.layout.dropdown_menu_popup_item,
                listAllBin)
        bottomSheetInternal!!.rootView.spinner4_dropdown.setText(spinnerSharedPref.getInt("SpinnerFrom", 10).toString())
        bottomSheetInternal!!.rootView.spinner5_dropdown.setText(spinnerSharedPref.getInt("SpinnerTo", 2).toString())

        val sym = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

        //10
        editText10.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var allowVal = ""
                for (i in 0 until 10)
                    allowVal += sym[i]
                if (editText10.hasFocus()) {
                    if (s.toString() != "" && !s!!.endsWith("."))
                        parse(s.toString(), editText10, textInputLayout10, 10, filled_exposed_dropdown.text.toString().toInt(), allowVal)
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
                var allowVal = ""
                for (i in 0 until 2)
                    allowVal += sym[i]
                if (editText2.hasFocus())
                    if (s.toString() != "" && !s!!.endsWith("."))
                        parse(s.toString(), editText2, textInputLayout2, 2, filled_exposed_dropdown.text.toString().toInt(), allowVal)
                if (s.toString() == "" && !cleared)
                    clearConverter(editText2)
            }
        })
        //8
        editText8.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var allowVal = ""
                for (i in 0 until 8)
                    allowVal += sym[i]
                if (editText8.hasFocus())
                    if (s.toString() != "" && !s!!.endsWith("."))
                        parse(s.toString(), editText8, textInputLayout8, 8, filled_exposed_dropdown.text.toString().toInt(), allowVal)
                if (s.toString() == "" && !cleared)
                    clearConverter(editText8)
            }
        })
        //16
        editText16.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var allowVal = ""
                for (i in 0 until 16)
                    allowVal += sym[i]
                if (editText16!!.hasFocus())
                    if (s.toString() != "" && !s!!.endsWith(".") && editText16.hasFocus())
                        parse(s.toString(), editText16, textInputLayout16, 16, filled_exposed_dropdown.text.toString().toInt(), allowVal)
                if (s.toString() == "" && !cleared)
                    clearConverter(editText16)
            }
        })
        //custom
        editTextCustom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var allowVal = ""
//                for (i in 0 until filled_exposed_dropdown.text.toString().toInt())
//                    allowVal += sym[i]
                editTextCustom!!.hasFocus()
                if (s.toString() != "" && !s!!.endsWith(".") && editTextCustom.hasFocus())
                    parse(s.toString(), editTextCustom, textInputLayoutCustom, filled_exposed_dropdown.text.toString().toInt(), null, allowVal)
                if (s.toString() == "" && !cleared)
                    clearConverter(editTextCustom)
            }
        })

        view.rootView.explanation_fab.setOnClickListener {
            bottomSheetInternal!!.rootView.spinner4_dropdown.setAdapter(adapter)
            bottomSheetInternal!!.rootView.spinner5_dropdown.setAdapter(adapter)
            if (editText10!!.text.toString().isNotEmpty()) {
                try {
                    val job = GlobalScope.launch(Dispatchers.Main) {
                        async {
                            bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                            bottomSheetDialog!!.show()
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.VISIBLE
                            delay(1)
                            bottomSheetDialog!!.behavior.peekHeight = bottomSheetInternal!!.rootView.bottomSheetCardView.height
                            delay(350)
                        }.join()
                        launch {
                            bottomSheetInternal!!.rootView.bottomSheetScrollView.setPadding(0, 0, 0, bottomSheetInternal!!.rootView.bottomSheetAppBar.height)
                            try {
                                parseRepeat()
                            } catch (e: Exception) {
                                bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                            }
                        }.join()
                        async {
                            delay(1)
                            bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                            bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE
                        }.join()
                    }
                    job.start()
                } catch (e: Exception) {
                    bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                }
            }
        }

        bottomSheetInternal!!.rootView.revertButton.setOnClickListener {
            val job = GlobalScope.launch(Dispatchers.Main) {
                async {
                    bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                    bottomSheetInternal!!.rootView.progressBar.visibility = View.VISIBLE
                    val buf = bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString()
                    bottomSheetInternal!!.rootView.spinner4_dropdown.setText(bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString())
                    bottomSheetInternal!!.rootView.spinner5_dropdown.setText(buf)
                    bottomSheetInternal!!.rootView.revertButton.setImageResource(R.drawable.compare)
                    spinnerEditor.putInt("SpinnerFrom", bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt()).apply()
                    spinnerEditor.putInt("SpinnerTo", bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt()).apply()
                    (bottomSheetInternal!!.rootView.revertButton.drawable as Animatable).start()
                    bottomSheetInternal!!.rootView.spinner4_dropdown.setAdapter(adapter)
                    bottomSheetInternal!!.rootView.spinner5_dropdown.setAdapter(adapter)
                    delay(350)
                }.join()
                async {
                    try {
                        if (editText10.text.toString() != "")
                            parseRepeat()
                    } catch (e: Exception) {
                        bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                    }
                }.join()
                async {
                    delay(1)
                    bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                    bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE
                }.join()
            }
            job.start()
        }

        bottomSheetInternal!!.rootView.spinner4_dropdown.setOnItemClickListener { _, _, _, _ ->
                val job = GlobalScope.launch(Dispatchers.Main) {
                    async {
                        bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                        bottomSheetInternal!!.rootView.progressBar.visibility = View.VISIBLE
                        spinnerEditor.putInt("SpinnerFrom", bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt()).apply()
                        spinnerEditor.putInt("SpinnerTo", bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt()).apply()
                        delay(350)
                    }.join()
                    launch {
                        if (bottomSheetDialog!!.behavior.state != STATE_HIDDEN)
                            try {
                                parseRepeat()
                            } catch (e: Exception) {
                                bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                            }
                    }.join()
                    async {
                        delay(1)
                        bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                        bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE
                    }.join()
                }
                job.start()
        }

        bottomSheetInternal!!.rootView.spinner5_dropdown.setOnItemClickListener { _, _, _, _ ->
            val job = GlobalScope.launch(Dispatchers.Main) {
                async {
                    bottomSheetDialog!!.behavior.state = STATE_COLLAPSED
                    bottomSheetInternal?.rootView?.progressBar?.visibility = View.VISIBLE
                    spinnerEditor.putInt("SpinnerFrom", bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt()).apply()
                    spinnerEditor.putInt("SpinnerTo", bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt()).apply()
                    delay(300)
                }.join()
                launch {
                    if (bottomSheetDialog!!.behavior.state != STATE_HIDDEN)
                        try {
                            parseRepeat()
                        } catch (e: Exception) {
                            bottomSheetDialog!!.behavior.state = STATE_HIDDEN
                        }
                }.join()
                async {
                    delay(1)
                    bottomSheetDialog!!.behavior.state = STATE_EXPANDED
                    bottomSheetInternal!!.rootView.progressBar.visibility = View.INVISIBLE
                }.join()
            }
            job.start()
            }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    @ExperimentalUnsignedTypes
    fun parse(string: String, editField: View, editLayout: TextInputLayout, bin: Int, custom: Int?, allowVal: String) = try {
        if (editField.resources.getResourceEntryName(editField.id) != "editText10")
            editText10.setText(ConvertTo().main(string, bin, 10))
        if (editField.resources.getResourceEntryName(editField.id) != "editText2")
            editText2.setText(ConvertTo().main(string, bin, 2))
        if (editField.resources.getResourceEntryName(editField.id) != "editText8")
            editText8.setText(ConvertTo().main(string, bin, 8))
        if (editField.resources.getResourceEntryName(editField.id) != "editText16")
            editText16.setText(ConvertTo().main(string, bin, 16))
        if (editField.resources.getResourceEntryName(editField.id) != "editTextCustom")
            editTextCustom.setText(ConvertTo().main(string, bin, custom))
        checkClear()
        cleared = false
        errorNull()
    } catch (e: Exception) {
        editLayout.error = getString(R.string.available_characters_for_input) + ": $allowVal"
    }

    @ExperimentalUnsignedTypes
    @SuppressLint("SetTextI18n", "InflateParams", "DefaultLocale")
    fun parseRepeat() {
        var i = 0
        var summ: BigDecimal

        var result = ""
        var resultRight = ""
        bottomSheetInternal!!.rootView.explanationCardViewConvert.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardView1.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardView2.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardView3.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardView4.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardView5.removeAllViews()
        bottomSheetInternal!!.rootView.explanationCardViewAnswer.removeAllViews()

        bottomSheetInternal!!.rootView.hsvConvert.visibility = View.GONE
        bottomSheetInternal!!.rootView.hsv1.visibility = View.GONE
        bottomSheetInternal!!.rootView.hsv2.visibility = View.GONE
        bottomSheetInternal!!.rootView.hsv3.visibility = View.GONE
        bottomSheetInternal!!.rootView.explanationCardView4.visibility = View.GONE
        bottomSheetInternal!!.rootView.hsv5.visibility = View.GONE
        bottomSheetInternal!!.rootView.hsvAnswer.visibility = View.GONE

        bottomSheetInternal!!.rootView.step1.visibility = View.GONE
        bottomSheetInternal!!.rootView.step2.visibility = View.GONE
        bottomSheetInternal!!.rootView.step3.visibility = View.GONE
        bottomSheetInternal!!.rootView.step4.visibility = View.GONE
        bottomSheetInternal!!.rootView.step5.visibility = View.GONE
        bottomSheetInternal!!.rootView.stepAnswer.visibility = View.GONE


        bottomSheetInternal!!.rootView.dividerExp1.visibility = View.GONE
        bottomSheetInternal!!.rootView.dividerExp2.visibility = View.GONE
        bottomSheetInternal!!.rootView.dividerExp3.visibility = View.GONE
        bottomSheetInternal!!.rootView.dividerExp4.visibility = View.GONE
        bottomSheetInternal!!.rootView.dividerExp5.visibility = View.GONE
        bottomSheetInternal!!.rootView.dividerExpAnswer.visibility = View.GONE

        // Блок с ответом
        val viConvert = layoutInflater.inflate(R.layout.answer_layout, null, false)
        viConvert.textResultExplanation1.text = ConvertTo().main(editText10.text.toString(), 10, bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt())
        viConvert.resultConvertNumber1.text = bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString()
        viConvert.textResultExplanation2.text = ConvertTo().main(editText10.text.toString(), 10, bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt())
        viConvert.resultConvertNumber2.text = bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString()
        bottomSheetInternal!!.rootView.explanationCardViewConvert.addView(viConvert)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(8, 8, 32, 8)

        if (((bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt() <= 36) && (bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt() <= 36)) && (bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt() != bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt())) {
            bottomSheetInternal!!.rootView.hsvConvert.visibility = View.VISIBLE

            bottomSheetInternal!!.rootView.step1.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.hsv1.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.dividerExp1.visibility = View.VISIBLE

            bottomSheetInternal!!.rootView.step2.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.hsv2.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.dividerExp2.visibility = View.VISIBLE

            bottomSheetInternal!!.rootView.step3.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.hsv3.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.dividerExp3.visibility = View.VISIBLE

            bottomSheetInternal!!.rootView.stepAnswer.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.hsvAnswer.visibility = View.VISIBLE
            bottomSheetInternal!!.rootView.dividerExpAnswer.visibility = View.VISIBLE


            val fromSpinner = bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString()
            val toSpinner = bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString()

            val stringOriginal = ConvertTo().main(editText10.text.toString(), 10, fromSpinner.toInt())
            val decArray: CharArray = stringOriginal.replace(".", "").toCharArray()
            val leftDecArray = stringOriginal.split(".")[0].toCharArray()

            var decLength = decArray.size
            var leftDecLength = leftDecArray.size
            if (fromSpinner.toInt() != 10)
                for (ch in decArray) {
                    decLength--
                    leftDecLength--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = fromSpinner
                    vi1.explanation_multiply_textView1.text = (leftDecLength).toString()
                    vi1.explanation_multiply_textView2.text = ch.toString().toBigInteger(fromSpinner.toInt()).toString(10)
                    if (decLength == 0) {
                        vi1.explanation_multiply_plus.text = " = "
                    }
                    bottomSheetInternal!!.rootView.explanationCardView1.addView(vi1)
                    i++
                }
            else {
                bottomSheetInternal!!.rootView.step1.visibility = View.GONE
                bottomSheetInternal!!.rootView.hsv1.visibility = View.GONE
                bottomSheetInternal!!.rootView.dividerExp1.visibility = View.GONE
            }

            decLength = decArray.size
            leftDecLength = leftDecArray.size
            summ = 0.toBigDecimal()
            for (ch in decArray) {
                decLength--
                leftDecLength--
                val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
                vi2.explanation_multiply2_textView1.text = (ch.toString().toBigInteger(fromSpinner.toInt()).toString(10).toBigDecimal().stripTrailingZeros() * fromSpinner.toDouble().pow(leftDecLength.toDouble()).toBigDecimal().stripTrailingZeros()).setScale(fractionCount, RoundingMode.DOWN).stripTrailingZeros().toString()
                vi2.explanation_multiply2_convert.text = "10"
                bottomSheetInternal!!.rootView.explanationCardView1.addView(vi2)
                summ += vi2.explanation_multiply2_textView1.text.toString().toBigDecimal().stripTrailingZeros()
                if (decLength == 0) {
                    vi2.explanation_multiply2_plus.text = "="
                }
            }
            val viRes = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
            viRes.explanation_multiply2_textView1.text = summ.toString()
            viRes.explanation_multiply2_convert.text = "10"
            viRes.explanation_multiply2_plus.text = ""
            bottomSheetInternal!!.rootView.explanationCardView1.addView(viRes)

            var workString = summ.toString().split(".")[0].toBigDecimal()

            if (workString < toSpinner.toBigDecimal()) {
                bottomSheetInternal!!.rootView.step2.visibility = View.GONE
                bottomSheetInternal!!.rootView.hsv2.visibility = View.GONE
                bottomSheetInternal!!.rootView.dividerExp2.visibility = View.GONE
                bottomSheetInternal!!.rootView.step3.visibility = View.GONE
                bottomSheetInternal!!.rootView.hsv3.visibility = View.GONE
                bottomSheetInternal!!.rootView.dividerExp3.visibility = View.GONE
                result += workString
            }

            while (workString > 0.toBigDecimal() && workString >= toSpinner.toBigDecimal()) {
                val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                var tv5String: String

                vi3.textView1.text = workString.toString() // Делимое
                vi3.textView2.text = toSpinner // Делитель

                vi3.textView4.text = (vi3.textView1.text.toString().toBigInteger() / toSpinner.toBigInteger()).toString().split(".")[0]// Целое
                vi3.textView3.text = (vi3.textView4.text.toString().toBigDecimal() * toSpinner.toBigDecimal()).toString() // То, что вычитаем
                tv5String = (vi3.textView1.text.toString().toBigDecimal() - vi3.textView3.text.toString().toBigDecimal()).toString()

                if (toSpinner.toInt() != 10) { //Если спиннер 2 равен 10, то не выводим View
                    if (tv5String.toInt() < 10) {
                        vi3.textView5.text = tv5String // Остаток
                        result += tv5String //Присваиваем результат
                    } else {
                        vi3.textView5.text = tv5String + "=" + tv5String.toBigInteger(10).toString(toSpinner.toInt()).toUpperCase()  // Остаток
                        result += tv5String.toBigInteger(10).toString(toSpinner.toInt()).toUpperCase() //Присваиваем результат
                    }
                } else {
                    bottomSheetInternal!!.rootView.step2.visibility = View.GONE
                    bottomSheetInternal!!.rootView.hsv2.visibility = View.GONE
                    bottomSheetInternal!!.rootView.dividerExp2.visibility = View.GONE

                    bottomSheetInternal!!.rootView.step3.visibility = View.GONE
                    bottomSheetInternal!!.rootView.hsv3.visibility = View.GONE
                    bottomSheetInternal!!.rootView.dividerExp3.visibility = View.GONE
                }


                workString = vi3.textView4.text.toString().toBigDecimal()
                if (workString < vi3.textView2.text.toString().toBigDecimal()) {
                    if (vi3.textView4.text.toString().toBigDecimal() >= 10.toBigDecimal()) {
                        result += vi3.textView4.text.toString().toBigInteger(10).toString(toSpinner.toInt()).toUpperCase()
                        vi3.textView4.text = vi3.textView4.text.toString() + "=" + vi3.textView4.text.toString().toBigInteger(10).toString(toSpinner.toInt()).toUpperCase()
                    } else {
                        result += vi3.textView4.text.toString()
                    }
                    bottomSheetInternal!!.rootView.explanationCardView2.addView(vi3)
                    break
                }
                bottomSheetInternal!!.rootView.explanationCardView2.addView(vi3)
            }
            val viReverse = layoutInflater.inflate(R.layout.multiply_layout4, null, false)
            viReverse.reverseTextView.text = result.reversed()
            viReverse.reverseNumberTextView.text = toSpinner
            bottomSheetInternal!!.rootView.explanationCardView3.addView(viReverse)

            //Если в текстовом поле есть точка, то выводим
            if (editText10.text.toString().contains(".") && toSpinner != "10" && summ.toString().split(".").size > 1) {
                bottomSheetInternal!!.rootView.step4.visibility = View.VISIBLE
                bottomSheetInternal!!.rootView.explanationCardView4.visibility = View.VISIBLE
                bottomSheetInternal!!.rootView.dividerExp4.visibility = View.VISIBLE
                bottomSheetInternal!!.rootView.step5.visibility = View.VISIBLE
                bottomSheetInternal!!.rootView.hsv5.visibility = View.VISIBLE
                bottomSheetInternal!!.rootView.dividerExp5.visibility = View.VISIBLE
                var z = 0
                var res = "0.0".toBigDecimal()
                var n1 = "0." + summ.toString().split(".")[1]
                val n2 = bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString()
                while (z < fractionCount + 1) {
                    val vi5 = layoutInflater.inflate(R.layout.multiply_layout3, null, false)
                    if (n1.split(".")[1] == "0") break // Если десятичное число равно нулю, то прерываем итерацию
                    if (z == 0) {
                        vi5.fractional_textView_result.text = ""
                        vi5.fractional_textView_1.text = "." + n1.split(".")[1]
                    } else {
                        if (n1.split(".")[1] == "0") z = fractionCount
                        try {
                            val dd = res.toString().split(".")[0].toBigInteger(10).toString(toSpinner.toInt()).toUpperCase()
                            if (res.toString().split(".")[0].toInt() < 10) {
                                vi5.fractional_textView_result.text = dd
                                resultRight += dd
                            } else {
                                vi5.fractional_textView_result.text = dd + "=" + res.toString().split(".")[0].toInt()
                                resultRight += dd
                            }
                            n1 = if (res.toString().contains("."))
                                "0." + res.toString().split(".")[1]
                            else
                                "0.0"
                            vi5.fractional_textView_1.text = "." + n1.split(".")[1]
                        } catch (e: Exception) {
                        }
                    }
                    vi5.fractional_textView_2.text = n2
                    bottomSheetInternal!!.rootView.explanationCardView4.addView(vi5)
                    if (!res.toString().contains(".")) break
                    res = (n1.toBigDecimal() * n2.toBigDecimal()).stripTrailingZeros()
                    z++
                }

                val viSum = layoutInflater.inflate(R.layout.frag_layout, null, false)
                viSum.frag1_textView.text = result.reversed()
                viSum.fragConvertNumber1.text = toSpinner
                viSum.frag2_textView.text = "0.$resultRight"
                viSum.fragConvertNumber2.text = toSpinner
                viSum.fragResult_textView.text = "${result.reversed()}.$resultRight"
                viSum.fragConvertNumber3.text = toSpinner
                bottomSheetInternal!!.rootView.explanationCardView5.addView(viSum)
            } else {
                bottomSheetInternal!!.rootView.step4.visibility = View.GONE
                bottomSheetInternal!!.rootView.explanationCardView4.visibility = View.GONE
                bottomSheetInternal!!.rootView.dividerExp4.visibility = View.GONE
                bottomSheetInternal!!.rootView.step5.visibility = View.GONE
                bottomSheetInternal!!.rootView.hsv5.visibility = View.GONE
                bottomSheetInternal!!.rootView.dividerExp5.visibility = View.GONE
            }
        }

        if (bottomSheetInternal!!.rootView.spinner4_dropdown.text.toString().toInt() == bottomSheetInternal!!.rootView.spinner5_dropdown.text.toString().toInt()) {
            bottomSheetInternal!!.rootView.hsvConvert.visibility = View.GONE
            bottomSheetInternal!!.rootView.hsv1.visibility = View.GONE
            bottomSheetInternal!!.rootView.hsv2.visibility = View.GONE
            bottomSheetInternal!!.rootView.hsv3.visibility = View.GONE
            bottomSheetInternal!!.rootView.explanationCardView4.visibility = View.GONE
            bottomSheetInternal!!.rootView.hsv5.visibility = View.GONE
            bottomSheetInternal!!.rootView.hsvAnswer.visibility = View.GONE
            bottomSheetInternal!!.rootView.step1.visibility = View.GONE
            bottomSheetInternal!!.rootView.step2.visibility = View.GONE
            bottomSheetInternal!!.rootView.step3.visibility = View.GONE
            bottomSheetInternal!!.rootView.step4.visibility = View.GONE
            bottomSheetInternal!!.rootView.step5.visibility = View.GONE
            bottomSheetInternal!!.rootView.stepAnswer.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExp1.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExp2.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExp3.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExp4.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExp5.visibility = View.GONE
            bottomSheetInternal!!.rootView.dividerExpAnswer.visibility = View.GONE
        }

        // Блок с ответом
        val viAnswer = layoutInflater.inflate(R.layout.answer_layout, null, false)
        viAnswer.textResultExplanation1.text = bottomSheetInternal!!.rootView.textResultExplanation1.text
        viAnswer.resultConvertNumber1.text = bottomSheetInternal!!.rootView.resultConvertNumber1.text
        viAnswer.textResultExplanation2.text = bottomSheetInternal!!.rootView.textResultExplanation2.text
        viAnswer.resultConvertNumber2.text = bottomSheetInternal!!.rootView.resultConvertNumber2.text
        bottomSheetInternal!!.rootView.explanationCardViewAnswer.addView(viAnswer)
        bottomSheetDialog!!.behavior.state = STATE_EXPANDED

    }

    fun clearConverter(editField: View?) {
        if (!cleared) {
            cleared = true
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText10") editText10?.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText2") editText2?.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText8") editText8?.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editText16") editText16?.setText("")
            if (editField?.resources?.getResourceEntryName(editField.id) != "editTextCustom") editTextCustom?.setText("")
        }
        errorNull()
        view!!.rootView.clear_fab.hide()
        view!!.rootView.explanation_fab.hide()
    }

    private fun errorNull() {
        textInputLayout10?.error = null
        textInputLayout2?.error = null
        textInputLayout8?.error = null
        textInputLayout16?.error = null
        textInputLayoutCustom?.error = null
    }

    private fun checkClear() {
        if (editText10.text.toString() != "" || editText2.text.toString() != "" || editText8.text.toString() != "" || editText16.text.toString() != "" || editTextCustom.text.toString() != "") {
            view!!.rootView.clear_fab.show()
            if (editText10.text.toString() != "1")
                view!!.rootView.explanation_fab.show()
            else
                view!!.rootView.explanation_fab.show()
        } else {
            view!!.rootView.clear_fab.hide()
            view!!.rootView.explanation_fab.hide()
            editText10.setText("")
            editText2.setText("")
            editText8.setText("")
            editText16.setText("")
            editTextCustom.setText("")
        }
    }

    override fun onStart() {
        super.onStart()
        val converterSave = context!!.getSharedPreferences("converter", Context.MODE_PRIVATE)
        val adapter = ArrayAdapter(
                context!!,
                R.layout.dropdown_menu_popup_item,
                listCustomBin)
        filled_exposed_dropdown.setText(converterSave.getInt("converterSpinner", 3).toString())
        filled_exposed_dropdown.setAdapter(adapter)
        editText10.setText(converterSave.getString("converter10", ""))

        if (editText10.text.toString() != "")
            parse(converterSave.getString("converter10", "")!!, view!!.rootView.editText10, view!!.rootView.textInputLayout10, 10, filled_exposed_dropdown.text.toString().toInt(), "0123456789")
        checkClear()
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = context!!.getSharedPreferences("converter", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("converter10", editText10.text.toString()).apply()
        editor.putInt("converterSpinner", filled_exposed_dropdown.text.toString().toInt()).apply()
    }
}