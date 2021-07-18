package ru.sandello.binaryconverter.ui.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.utils.*
import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorViewModel : ViewModel() {
    private val _operand1 = MutableLiveData(0.toBigDecimal())
    val operand1: LiveData<BigDecimal> = _operand1
    private val _operand2 = MutableLiveData(0.toBigDecimal())
    val operand2: LiveData<BigDecimal> = _operand2
    private val _result = MediatorLiveData<BigDecimal>()
    val result: LiveData<BigDecimal> = _result
    private val _fraction1 = MutableLiveData(10)
    val fraction1: LiveData<Int> = _fraction1
    private val _fraction2 = MutableLiveData(10)
    val fraction2: LiveData<Int> = _fraction2
    private val _fractionResult = MutableLiveData(10)
    val fractionResult: LiveData<Int> = _fractionResult
    private val _actions = MutableLiveData(CalcActions.PLUS)
    val actions: LiveData<CalcActions> = _actions

    val showInvalidInputError = MutableLiveData<Pair<Int, String>>()
    val stringToast = MutableLiveData<String>()

    private var myClip: ClipData? = null
    private var myClipboard: ClipboardManager? = null

    private val listCustomBin = arrayOf(
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
        _result.addSource(_operand1) { calculate() }
        _result.addSource(_operand2) { calculate() }
        _result.addSource(_fraction1) { calculate() }
        _result.addSource(_fraction2) { calculate() }
        _result.addSource(_fractionResult) { calculate() }
        _result.addSource(_actions) { calculate() }
    }

    fun allow(to: Int): String {
        var allowVal = ""
        for (i in 0 until to)
            allowVal += sym[i]
        return allowVal
    }

    /*  private fun errorNull() {
          binding.textInputLayoutCustom1.error = null
          binding.textInputLayoutCustom2.error = null
          binding.textInputLayoutResult.error = null
      }*/

    /*private fun checkClear() {
        if (binding.editTextVal1.text.toString() != "" || binding.editTextVal2.text.toString() != "")
            binding.clear_fab.show()
        else
            binding.clear_fab.hide()
    }*/

    fun copyVal(value: String) {
        if (value == "calResult" && result.value.toString() != "0" && result.value.toString() != "") {
            myClip = ClipData.newPlainText("text", result.value.toString())
            myClipboard!!.setPrimaryClip(myClip!!)

            stringToast.postValue("${Shared.resourcesHelper.getString(R.string.copied)}: ${_result.value}")
        }
    }

    fun updateOperand(action: Int, value: String = "", fraction: Int) {
        try {
            when (action) {
                1 -> _operand1.postValue(ConvertTo().main(value, fraction, 10).toBigDecimal())
                2 -> _operand2.postValue(ConvertTo().main(value, fraction, 10).toBigDecimal())
            }
        } catch (e: Exception) {
            showInvalidInputError.postValue(Pair(action, allow(fraction)))
        }
        Log.i(
            APP_TAG,
            "CalculatorViewModel::updateOperand act: $action val: $value, frac: $fraction"
        )
    }

    fun updateFraction(fractionId: Int, newFraction: Int) {
        when (fractionId) {
            1 -> _fraction1.postValue(newFraction)
            2 -> _fraction2.postValue(newFraction)
            3 -> _fractionResult.postValue(newFraction)
        }

        Log.i(APP_TAG, "CalculatorViewModel::updateFraction id: $fractionId val: $newFraction")
    }

    fun updateAction(newAction: CalcActions) {
        _actions.postValue(newAction)
        Log.i(APP_TAG, "CalculatorViewModel::updateAction act: $newAction")
    }

    private fun calculate() {
        when (_actions.value) {
            CalcActions.PLUS -> _result.postValue(_operand1.value?.add(_operand2.value))
            CalcActions.MINUS -> _result.postValue(_operand1.value?.min(_operand2.value))
            CalcActions.MULTIPLY -> _result.postValue(_operand1.value?.multiply(_operand2.value))
            CalcActions.DIVIDE -> _result.postValue(
                operand1.value?.divide(
                    operand2.value,
                    _fractionResult.value ?: 10,
                    RoundingMode.HALF_UP
                )
            )
        }
    }

    fun load() {
        _operand1.postValue(
            Shared.preferencesHelper.getStringFromPrefs(SHARED_CALC_OPERAND_1)?.toBigDecimal()
        )
        _operand2.postValue(
            Shared.preferencesHelper.getStringFromPrefs(SHARED_CALC_OPERAND_2)?.toBigDecimal()
        )
        _actions.postValue(
            CalcActions.valueOf(
                Shared.preferencesHelper.getStringFromPrefs(
                    SHARED_CALC_ACTIONS
                ) ?: CalcActions.PLUS.toString()
            )
        )
        _fraction1.postValue(Shared.preferencesHelper.getIntFromPrefs(SHARED_CALC_FRACTION_1))
        _fraction2.postValue(Shared.preferencesHelper.getIntFromPrefs(SHARED_CALC_FRACTION_2))
        _fractionResult.postValue(
            Shared.preferencesHelper.getIntFromPrefs(
                SHARED_CALC_FRACTION_RESULT
            )
        )
    }

    fun save() {
        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_OPERAND_1, operand1.value.toString())
        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_OPERAND_2, operand2.value.toString())
        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_ACTIONS, actions.value.toString())
        fraction1.value?.let { Shared.preferencesHelper.intToPrefs(SHARED_CALC_FRACTION_1, it) }
        fraction2.value?.let { Shared.preferencesHelper.intToPrefs(SHARED_CALC_FRACTION_2, it) }
        fractionResult.value?.let {
            Shared.preferencesHelper.intToPrefs(
                SHARED_CALC_FRACTION_RESULT,
                it
            )
        }
    }
}