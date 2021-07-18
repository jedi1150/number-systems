package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.FragmentExplanationBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class ExplanationFragment : Fragment() {
    private lateinit var binding: FragmentExplanationBinding
    private val model: ExplanationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExplanationBinding.inflate(layoutInflater)
        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

 /*   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetInternal!!.rootView.spinner4_dropdown.setText(
            spinnerSharedPref.getInt(
                "SpinnerFrom",
                10
            ).toString()
        )
        bottomSheetInternal!!.rootView.spinner5_dropdown.setText(
            spinnerSharedPref.getInt(
                "SpinnerTo",
                2
            ).toString()
        )
        bottomSheetInternal!!.rootView.spinner4_dropdown.setAdapter(adapter)
        bottomSheetInternal!!.rootView.spinner5_dropdown.setAdapter(adapter)

        /// TODO on revert button click
        binding.revertButton.setOnClickListener {
            val job = GlobalScope.launch(Dispatchers.Main) {
                async {
                    binding.bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.progressBar.visibility = View.VISIBLE
                    val buf = binding.spinner4Dropdown.text.toString()
                    binding.spinner4Dropdown.setText(binding.spinner5Dropdown.text.toString())
                    binding.spinner5Dropdown.setText(buf)
                    adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_menu_popup_item,
                        listAllBin
                    )
                    binding.spinner4Dropdown.setAdapter(adapter)
                    binding.spinner5Dropdown.setAdapter(adapter)
                    binding.revertButton.setImageResource(R.drawable.compare)
                    spinnerEditor.putInt(
                        "SpinnerFrom",
                        binding.spinner4Dropdown.text.toString().toInt()
                    ).apply()
                    spinnerEditor.putInt(
                        "SpinnerTo",
                        binding.spinner5Dropdown.text.toString().toInt()
                    ).apply()
                    (binding.revertButton.drawable as Animatable).start()

                    delay(350)
                }.join()
                async {
                    try {
                        if (editText10!!.text.toString() != "")
                            parseRepeat()
                    } catch (e: Exception) {
                        bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }.join()
                async {
                    delay(1)
                    if (binding.spinner4Dropdown.text.toString()
                            .toInt() != binding.spinner5Dropdown.text.toString()
                            .toInt()
                    ) {
                        bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                }.join()
            }
            job.start()
        }

        binding.spinner4Dropdown.setOnItemClickListener { _, _, _, _ ->
            val job = GlobalScope.launch(Dispatchers.Main) {
                async {
                    bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.progressBar.visibility = View.VISIBLE
                    spinnerEditor.putInt(
                        "SpinnerFrom",
                        binding.spinner4Dropdown.text.toString().toInt()
                    ).apply()
                    spinnerEditor.putInt(
                        "SpinnerTo",
                        binding.spinner5Dropdown.text.toString().toInt()
                    ).apply()
                    delay(350)
                }.join()
                launch {
                    if (bottomSheetDialog!!.behavior.state != BottomSheetBehavior.STATE_HIDDEN)
                        try {
                            parseRepeat()
                        } catch (e: Exception) {
                            bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                }.join()
                async {
                    delay(1)
                    if (binding.spinner4Dropdown.text.toString()
                            .toInt() != binding.spinner5Dropdown.text.toString()
                            .toInt()
                    ) {
                        bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                }.join()
            }
            job.start()
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                listAllBin
            )
        }

        binding.spinner5Dropdown.setOnItemClickListener { _, _, _, _ ->
            val job = GlobalScope.launch(Dispatchers.Main) {
                async {
                    bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.progressBar?.visibility = View.VISIBLE
                    spinnerEditor.putInt(
                        "SpinnerFrom",
                        binding.spinner4Dropdown.text.toString().toInt()
                    ).apply()
                    spinnerEditor.putInt(
                        "SpinnerTo",
                        binding.spinner5Dropdown.text.toString().toInt()
                    ).apply()
                    delay(300)
                }.join()
                launch {
                    if (bottomSheetDialog!!.behavior.state != BottomSheetBehavior.STATE_HIDDEN)
                        try {
                            parseRepeat()
                        } catch (e: Exception) {
                            bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                }.join()
                async {
                    delay(1)
                    if (binding.spinner4Dropdown.text.toString()
                            .toInt() != binding.spinner5Dropdown.text.toString()
                            .toInt()
                    ) {
                        bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                }.join()
            }
            job.start()
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                listAllBin
            )
        }

    }

    @SuppressLint("SetTextI18n", "InflateParams")
    fun parseRepeat() {
        var i = 0
        var summ: BigDecimal

        var result = ""
        var resultRight = ""
        binding.explanationCardViewConvert.removeAllViews()
        binding.explanationCardView1.removeAllViews()
        binding.explanationCardView2.removeAllViews()
        binding.explanationCardView3.removeAllViews()
        binding.explanationCardView4.removeAllViews()
        binding.explanationCardView5.removeAllViews()
        binding.explanationCardViewAnswer.removeAllViews()

        binding.hsvConvert.visibility = View.GONE
        binding.hsv1.visibility = View.GONE
        binding.hsv2.visibility = View.GONE
        binding.hsv3.visibility = View.GONE
        binding.explanationCardView4.visibility = View.GONE
        binding.hsv5.visibility = View.GONE
        binding.hsvAnswer.visibility = View.GONE

        binding.step1.visibility = View.GONE
        binding.step2.visibility = View.GONE
        binding.step3.visibility = View.GONE
        binding.step4.visibility = View.GONE
        binding.step5.visibility = View.GONE
        binding.stepAnswer.visibility = View.GONE


        binding.dividerExp1.visibility = View.GONE
        binding.dividerExp2.visibility = View.GONE
        binding.dividerExp3.visibility = View.GONE
        binding.dividerExp4.visibility = View.GONE
        binding.dividerExp5.visibility = View.GONE
        binding.dividerExpAnswer.visibility = View.GONE

        // Блок с ответом
        val viConvert = layoutInflater.inflate(R.layout.answer_layout, null, false)
        viConvert.textResultExplanation1.text = ConvertTo().main(
            editText10.text.toString(),
            10,
            binding.spinner4Dropdown.text.toString().toInt()
        )
        viConvert.resultConvertNumber1.text =
            binding.spinner4Dropdown.text.toString()
        viConvert.textResultExplanation2.text = ConvertTo().main(
            editText10.text.toString(),
            10,
            binding.spinner5Dropdown.text.toString().toInt()
        )
        viConvert.resultConvertNumber2.text =
            binding.spinner5Dropdown.text.toString()
        binding.explanationCardViewConvert.addView(viConvert)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.setMargins(8, 8, 32, 8)

        if (((binding.spinner4Dropdown.text.toString()
                .toInt() <= 36) && (binding.spinner5Dropdown.text.toString()
                .toInt() <= 36)) && (binding.spinner4Dropdown.text.toString()
                .toInt() != binding.spinner5Dropdown.text.toString()
                .toInt())
        ) {
            binding.hsvConvert.visibility = View.VISIBLE

            binding.step1.visibility = View.VISIBLE
            binding.hsv1.visibility = View.VISIBLE
            binding.dividerExp1.visibility = View.VISIBLE

            binding.step2.visibility = View.VISIBLE
            binding.hsv2.visibility = View.VISIBLE
            binding.dividerExp2.visibility = View.VISIBLE

            binding.step3.visibility = View.VISIBLE
            binding.hsv3.visibility = View.VISIBLE
            binding.dividerExp3.visibility = View.VISIBLE

            binding.stepAnswer.visibility = View.VISIBLE
            binding.hsvAnswer.visibility = View.VISIBLE
            binding.dividerExpAnswer.visibility = View.VISIBLE


            val fromSpinner = binding.spinner4Dropdown.text.toString()
            val toSpinner = binding.spinner5Dropdown.text.toString()

            val stringOriginal =
                ConvertTo().main(editText10.text.toString(), 10, fromSpinner.toInt())
            val decArray: CharArray = stringOriginal.replace("[,.]".toRegex(), "").toCharArray()
            val leftDecArray = stringOriginal.split("[,.]".toRegex())[0].toCharArray()

            var decLength = decArray.size
            var leftDecLength = leftDecArray.size
            if (fromSpinner.toInt() != 10)
                for (ch in decArray) {
                    decLength--
                    leftDecLength--
                    val vi1 = layoutInflater.inflate(R.layout.multiply_layout, null, false)
                    vi1.explanation_multiply_textView.text = fromSpinner
                    vi1.explanation_multiply_textView1.text = (leftDecLength).toString()
                    vi1.explanation_multiply_textView2.text =
                        ch.toString().toBigInteger(fromSpinner.toInt()).toString(10)
                    if (decLength == 0) {
                        vi1.explanation_multiply_plus.text = " = "
                    }
                    binding.explanationCardView1.addView(vi1)
                    i++
                }
            else {
                binding.step1.visibility = View.GONE
                binding.hsv1.visibility = View.GONE
                binding.dividerExp1.visibility = View.GONE
            }

            decLength = decArray.size
            leftDecLength = leftDecArray.size
            summ = 0.toBigDecimal()
            for (ch in decArray) {
                decLength--
                leftDecLength--
                val vi2 = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
                vi2.explanation_multiply2_textView1.text =
                    (ch.toString().toBigInteger(fromSpinner.toInt()).toString(10).toBigDecimal()
                        .stripTrailingZeros() * fromSpinner.toDouble().pow(leftDecLength.toDouble())
                        .toBigDecimal().stripTrailingZeros()).setScale(
                        Shared.FRACTION_COUNT,
                        RoundingMode.DOWN
                    ).stripTrailingZeros().toString()
                vi2.explanation_multiply2_convert.text = "10"
                binding.explanationCardView1.addView(vi2)
                summ += vi2.explanation_multiply2_textView1.text.toString().toBigDecimal()
                    .stripTrailingZeros()
                if (decLength == 0 && i > 1) {
                    vi2.explanation_multiply2_plus.text = " = "
                } else if (decLength == 0 && i == 1) {
                    vi2.explanation_multiply2_plus.text = ""
                }
            }
            val viRes = layoutInflater.inflate(R.layout.multiply_layout2, null, false)
            if (i > 1) {
                viRes.explanation_multiply2_textView1.text = summ.toString()
                viRes.explanation_multiply2_convert.text = "10"
                viRes.explanation_multiply2_plus.text = ""
                binding.explanationCardView1.addView(viRes)
            }

            var workString = summ.toString().split("[,.]".toRegex())[0].toBigDecimal()

            if (workString < toSpinner.toBigDecimal()) {
                binding.step2.visibility = View.GONE
                binding.hsv2.visibility = View.GONE
                binding.dividerExp2.visibility = View.GONE
                binding.step3.visibility = View.GONE
                binding.hsv3.visibility = View.GONE
                binding.dividerExp3.visibility = View.GONE
                result += workString
            }

            while (workString > 0.toBigDecimal() && workString >= toSpinner.toBigDecimal()) {

                val vi3 = layoutInflater.inflate(R.layout.div_layout, null, false)
                var tv5String: String

                vi3.textView1.text = workString.toString() // Делимое
                vi3.textView2.text = toSpinner // Делитель
                vi3.textView4.text =
                    (workString.toString().toBigInteger() / toSpinner.toBigInteger()).toString()
                        .split("[,.]".toRegex())[0]// Целое
                vi3.textView3.text = (vi3.textView4.text.toString()
                    .toBigDecimal() * toSpinner.toBigDecimal()).toString() // То, что вычитаем
                tv5String =
                    (vi3.textView1.text.toString().toBigDecimal() - vi3.textView3.text.toString()
                        .toBigDecimal()).toString()

                if (toSpinner.toInt() != 10) { //Если спиннер 2 равен 10, то не выводим View
                    if (tv5String.toInt() < 10) {
                        vi3.textView5.text = tv5String // Остаток
                        result += tv5String //Присваиваем результат
                    } else {
                        vi3.textView5.text =
                            tv5String + "=" + tv5String.toBigInteger(10).toString(toSpinner.toInt())
                                .toUpperCase(
                                    Locale.getDefault()
                                )  // Остаток
                        result += tv5String.toBigInteger(10).toString(toSpinner.toInt())
                            .toUpperCase(
                                Locale.getDefault()
                            ) //Присваиваем результат
                    }
                } else {
                    binding.step2.visibility = View.GONE
                    binding.hsv2.visibility = View.GONE
                    binding.dividerExp2.visibility = View.GONE

                    binding.step3.visibility = View.GONE
                    binding.hsv3.visibility = View.GONE
                    binding.dividerExp3.visibility = View.GONE
                }


                workString = vi3.textView4.text.toString().toBigDecimal()
                if (workString < vi3.textView2.text.toString().toBigDecimal()) {
                    if (vi3.textView4.text.toString().toBigDecimal() >= 10.toBigDecimal()) {
                        result += vi3.textView4.text.toString().toBigInteger(10)
                            .toString(toSpinner.toInt()).toUpperCase(
                                Locale.getDefault()
                            )
                        vi3.textView4.text =
                            vi3.textView4.text.toString() + "=" + vi3.textView4.text.toString()
                                .toBigInteger(10).toString(toSpinner.toInt()).toUpperCase(
                                    Locale.getDefault()
                                )
                    } else {
                        result += vi3.textView4.text.toString()
                    }
                    binding.explanationCardView2.addView(vi3)
                    break
                }
                binding.explanationCardView2.addView(vi3)
            }
            val viReverse = layoutInflater.inflate(R.layout.multiply_layout4, null, false)
            viReverse.reverseTextView.text = result.reversed()
            viReverse.reverseNumberTextView.text = toSpinner
            binding.explanationCardView3.addView(viReverse)

            //Если в текстовом поле есть точка, то выводим
            if (editText10.text.toString()
                    .contains("[,.]".toRegex()) && toSpinner != "10" && summ.toString()
                    .split("[,.]".toRegex()).size > 1
            ) {
                binding.step4.visibility = View.VISIBLE
                binding.explanationCardView4.visibility = View.VISIBLE
                binding.dividerExp4.visibility = View.VISIBLE
                binding.step5.visibility = View.VISIBLE
                binding.hsv5.visibility = View.VISIBLE
                binding.dividerExp5.visibility = View.VISIBLE
                var z = 0
                var res = "0.0".toBigDecimal()
                var n1 = "0." + summ.toString().split("[,.]".toRegex())[1]
                val n2 = binding.spinner5Dropdown.text.toString()
                while (z < Shared.FRACTION_COUNT + 1) {
                    val vi5 = layoutInflater.inflate(R.layout.multiply_layout3, null, false)
                    if (n1.split("[,.]".toRegex())[1] == "0") break // Если десятичное число равно нулю, то прерываем итерацию
                    if (z == 0) {
                        vi5.fractional_textView_result.text = ""
                        vi5.fractional_textView_1.text = "." + n1.split("[,.]".toRegex())[1]
                    } else {
                        if (n1.split("[,.]".toRegex())[1] == "0") z = Shared.FRACTION_COUNT
                        try {
                            val dd = res.toString().split("[,.]".toRegex())[0].toBigInteger(10)
                                .toString(toSpinner.toInt()).toUpperCase(
                                    Locale.getDefault()
                                )
                            if (res.toString().split("[,.]".toRegex())[0].toInt() < 10) {
                                vi5.fractional_textView_result.text = dd
                                resultRight += dd
                            } else {
                                vi5.fractional_textView_result.text =
                                    dd + "=" + res.toString().split("[,.]".toRegex())[0].toInt()
                                resultRight += dd
                            }
                            n1 = if (res.toString().contains("[,.]".toRegex()))
                                "0." + res.toString().split("[,.]".toRegex())[1]
                            else
                                "0.0"
                            vi5.fractional_textView_1.text = "." + n1.split("[,.]".toRegex())[1]
                        } catch (e: Exception) {
                        }
                    }
                    vi5.fractional_textView_2.text = n2
                    binding.explanationCardView4.addView(vi5)
                    if (!res.toString().contains("[,.]".toRegex())) break
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
                binding.explanationCardView5.addView(viSum)
            } else {
                binding.step4.visibility = View.GONE
                binding.explanationCardView4.visibility = View.GONE
                binding.dividerExp4.visibility = View.GONE
                binding.step5.visibility = View.GONE
                binding.hsv5.visibility = View.GONE
                binding.dividerExp5.visibility = View.GONE
            }
        }

        if (binding.spinner4Dropdown.text.toString()
                .toInt() == binding.spinner5Dropdown.text.toString().toInt()
        ) {
            binding.hsvConvert.visibility = View.GONE
            binding.hsv1.visibility = View.GONE
            binding.hsv2.visibility = View.GONE
            binding.hsv3.visibility = View.GONE
            binding.explanationCardView4.visibility = View.GONE
            binding.hsv5.visibility = View.GONE
            binding.hsvAnswer.visibility = View.GONE
            binding.step1.visibility = View.GONE
            binding.step2.visibility = View.GONE
            binding.step3.visibility = View.GONE
            binding.step4.visibility = View.GONE
            binding.step5.visibility = View.GONE
            binding.stepAnswer.visibility = View.GONE
            binding.dividerExp1.visibility = View.GONE
            binding.dividerExp2.visibility = View.GONE
            binding.dividerExp3.visibility = View.GONE
            binding.dividerExp4.visibility = View.GONE
            binding.dividerExp5.visibility = View.GONE
            binding.dividerExpAnswer.visibility = View.GONE
        }

        // Блок с ответом
        val viAnswer = layoutInflater.inflate(R.layout.answer_layout, null, false)
        viAnswer.textResultExplanation1.text =
            binding.textResultExplanation1.text
        viAnswer.resultConvertNumber1.text =
            binding.resultConvertNumber1.text
        viAnswer.textResultExplanation2.text =
            binding.textResultExplanation2.text
        viAnswer.resultConvertNumber2.text =
            binding.resultConvertNumber2.text
        binding.explanationCardViewAnswer.addView(viAnswer)
    }*/
}