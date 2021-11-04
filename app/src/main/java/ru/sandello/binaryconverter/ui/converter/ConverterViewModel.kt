package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.ConvertTo
import java.math.BigDecimal

class ConverterViewModel : ViewModel() {

    private val _operand10new = mutableStateOf(TextFieldValue())
    val operand10new: State<TextFieldValue>
        get() = _operand10new
    private val _operand2new = mutableStateOf(TextFieldValue())
    val operand2new: State<TextFieldValue>
        get() = _operand2new
    private val _operand8new = mutableStateOf(TextFieldValue())
    val operand8new: State<TextFieldValue>
        get() = _operand8new
    private val _operand16new = mutableStateOf(TextFieldValue())
    val operand16new: State<TextFieldValue>
        get() = _operand16new
    private val _operandCustomNew = mutableStateOf(TextFieldValue())
    val operandCustomNew: State<TextFieldValue>
        get() = _operandCustomNew
    private val _customBaseNumber = mutableStateOf(3)
    val customBaseNumber: State<Int>
        get() = _customBaseNumber

    private val baseNumbers = IntArray(36) { it + 1 }
    val customBaseNumbers = baseNumbers.toMutableList().filter { !listOf(1, 2, 8, 10, 16).contains(it) }

    private val _operandBase = MediatorLiveData<BigDecimal>()
    val operandBase: LiveData<BigDecimal> = _operandBase

    val operandTest = MutableLiveData<String>()
    private val _operand10 = MutableLiveData(0.toBigDecimal())
    val operand10: LiveData<BigDecimal> = _operand10
    private val _operand2 = MutableLiveData(0.toBigDecimal())
    val operand2: LiveData<BigDecimal> = _operand2
    private val _operand8 = MutableLiveData(0.toBigDecimal())
    val operand8: LiveData<BigDecimal> = _operand8
    private val _operand16 = MutableLiveData(0.toBigDecimal())
    val operand16: LiveData<BigDecimal> = _operand16
    val _operandCustom = MutableLiveData(0.toBigDecimal())
    val operandCustom: LiveData<BigDecimal> = _operandCustom
    private val _fractionCustom = MutableLiveData(10)
    val fractionCustom: LiveData<Int> = _fractionCustom

    val showInvalidInputError = MutableLiveData<Pair<Int, String>>()
    val stringToast = MutableLiveData<String>()

    private val listCustomBin = arrayOf(
        3,
        4,
        5,
        6,
        7,
        9,
        11,
        12,
        13,
        14,
        15,
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
        29,
        30,
        31,
        32,
        33,
        34,
        35,
        36,
    )
    private val listAllBin = arrayOf(
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
        29,
        30,
        31,
        32,
        33,
        34,
        35,
        36,
    )
    private val sym = arrayOf(
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
    )

    init {
//        load()
//        _operandBase.addSource(_operandBase) {
//            parse()
//            Log.d("test228", "operandBase updated")
//        }
//        _operandBase.addSource(operandTest) {
//            Log.d("tet228 test", it)
//            Log.d("tet228 test", _operandBase.value.toString())
//            Log.d("tet228 test", _operandBase.value.toString())
//            viewModelScope.launch {
//                withContext(Dispatchers.Main) {
//                    val convertedResult =
//                        withContext(Dispatchers.Default) {
//                            ConvertTo().main(
//                                operandBase.value.toString(),
//                                10,
//                                2
//                            )
//                        }
//
//                    Log.d("tet228 test", convertedResult)
//                    if (convertedResult != _operandBase.value.toString())
//                        _operandBase.postValue(convertedResult.toBigDecimal())
//                    Log.d("tet228 _operandBase", _operandBase.value.toString())
//                    parse()
//                }
//
//            }
//        }
    }

    fun updateOperand(fraction: Int, textFieldValue: TextFieldValue) {
        try {
            Log.d(APP_TAG, "ConverterViewModel::updateOperand: frac: $fraction, textFieldVal: $textFieldValue")
//            val res16 = ConvertTo().main(value.text, fraction, 16).toBigDecimal()
            _operand10new.value = if (fraction != 10) TextFieldValue(ConvertTo().main(textFieldValue.text, fraction, 10)) else textFieldValue
            _operand2new.value = if (fraction != 2) TextFieldValue(ConvertTo().main(textFieldValue.text, fraction, 2)) else textFieldValue
            _operand8new.value = if (fraction != 8) TextFieldValue(ConvertTo().main(textFieldValue.text, fraction, 8)) else textFieldValue
            _operand16new.value = if (fraction != 16) TextFieldValue(ConvertTo().main(textFieldValue.text, fraction, 16)) else textFieldValue
            _operandCustomNew.value = if (fraction != _customBaseNumber.value) TextFieldValue(ConvertTo().main(textFieldValue.text, fraction, _customBaseNumber.value)) else textFieldValue
        } catch (e: Exception) {
            showInvalidInputError.postValue(Pair(fraction, allow(fraction)))
        }
        Log.i(
            APP_TAG,
            "ConverterViewModel::updateOperand val: $textFieldValue, frac: $fraction"
        )
    }

    fun updateCustomBaseNumber(newBase: Int) {
        Log.i(APP_TAG, "ConverterViewModel::updateCustomBaseNumber ${_customBaseNumber.value} to $newBase")
        _customBaseNumber.value = newBase
    }

    /*private fun parse() {
        if (_operandBase.value != null) {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.Main) {
                    viewModelScope.launch {
                        val resultTest =
                            withContext(Dispatchers.Default) {
                                ConvertTo().main(
                                    _operandBase.value.toString(),
                                    10,
                                    2
                                )
                            }
                        Log.d("test228 1", resultTest)
                        Log.d("test228 2", operandTest.value.toString())
                        if (resultTest != operandTest.value.toString()
                        ) {
                            operandTest.postValue(resultTest)
                        }
                    }
                }
            }
//            _operand10.postValue(
//                ConvertTo().main(_operandBase.value.toString(), 10, 10).toBigDecimal()
//            )
            _operand2.postValue(
                ConvertTo().main(operandBase.value.toString(), 10, 2).toBigDecimal()
            )
            _operand8.postValue(
                ConvertTo().main(operandBase.value.toString(), 10, 8).toBigDecimal()
            )
//            _operand16.postValue(
//                ConvertTo().main(_operandBase.value.toString(), 10, 16).toBigDecimal()
//            )
            _operandCustom.postValue(
                ConvertTo().main(
                    _operand10.value.toString(),
                    10,
                    fractionCustom.value
                ).toBigDecimal()
            )
        }
    }*/

    fun allow(to: Int): String {
        var allowVal = ""
        for (i in 0 until to)
            allowVal += sym[i]
        return allowVal
    }

/*    fun load() {
        Log.d(
            "test228",
            (Shared.preferencesHelper.getStringFromPrefs(SHARED_CONV_OPERAND)?.toBigDecimal()
                ?: 0.toBigDecimal().toString()).toString()
        )
        _operandBase.postValue(
            Shared.preferencesHelper.getStringFromPrefs(SHARED_CONV_OPERAND)?.toBigDecimal()
                ?: 0.toBigDecimal()
        )
        Log.d("test228", _operand10.value.toString())

        _fractionCustom.postValue(
            Shared.preferencesHelper.getIntFromPrefs(
                SHARED_CONV_FRACTION_CUSTOM
            )
        )

//        filled_exposed_dropdown.setText(converterSave.getInt("converterSpinner", 3).toString())
//        filled_exposed_dropdown.setAdapter(adapter)
//        if (editText10.text.toString() != "")
//            parse(
//                converterSave.getString("converter10", "")!!,
//                requireView().rootView.editText10,
//                requireView().rootView.textInputLayout10,
//                10,
//                filled_exposed_dropdown.text.toString().toInt(),
//                "0123456789"
//            )
//        checkClear()
    }*/

    /*fun save() {
        Shared.preferencesHelper.stringToPrefs(SHARED_CONV_OPERAND, operand10.value.toString())
        fractionCustom.value?.let {
            Shared.preferencesHelper.intToPrefs(
                SHARED_CONV_FRACTION_CUSTOM,
                it
            )
        }
    }*/
}