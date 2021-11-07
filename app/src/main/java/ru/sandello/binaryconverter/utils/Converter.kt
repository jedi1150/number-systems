package ru.sandello.binaryconverter.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.sandello.binaryconverter.model.ConvertedData
import java.util.*
import kotlin.math.pow

class Converter {

    private var string = ""
    private var leftLength = 0 //Длина целой части числа
    private var result = 0.toBigDecimal() //Переменная суммы
    private var returnString = "" //Возвращаемая строка
    private var fromParse = 0
    private var toParse = 0

    private var decArray = charArrayOf()
    private var leftDecArray = charArrayOf()
    private var rightDecArray = charArrayOf()
    private var minusBool = false

    operator fun invoke(value: String, fromRadix: Int, toRadix: Int): Flow<ConvertedData> = flow {
        Log.d(APP_TAG, "Converter:: invoke")

        assert(fromRadix > 2 || fromRadix < 36 || toRadix > 2 || fromRadix < 36) {
            "Radix must be greater than 2 and smaller than 36"
        }

        if (fromRadix == toRadix) {
            emit(ConvertedData(result = value, fromRadix = fromRadix, toRadix = toRadix))
            return@flow
        }

        emit(ConvertedData(result = convert(value, fromRadix, toRadix), fromRadix = fromRadix, toRadix = toRadix))
    }

    private fun convert(value: String, fromRadix: Int, toRadix: Int): String {
        Log.d(APP_TAG, "Converter:: convert: fromRadix: $fromRadix, toRadix: $toRadix")

        if (value.contains("-")) {
            string = value.replace("-", "")
            minusBool = true
        } else string = value
        fromParse = fromRadix
        toParse = toRadix

        leftDecArray = string.split("[,.]".toRegex())[0].toCharArray()
        try {
            rightDecArray = string.split("[,.]".toRegex())[1].toCharArray()
        } catch (e: Exception) {
        }
        decArray = string.replace("[,.]".toRegex(), "").toCharArray() //Строка без разделителя

        if (fromParse != 10 && toParse == 10) returnString = toDec()
        if (fromParse == 10 && toParse != 10) returnString = fromDec()
        if (fromParse == 10 && toParse == 10) returnString = fromDec()
        if (fromParse != 10 && toParse != 10) returnString = notDec()
        if (minusBool) {
            returnString = returnString.replaceRange(0, 0, "-")
            minusBool = false
        }
        return returnString
    }

    private fun toDec(): String {
        Log.d(APP_TAG, "Converter:: toDec")

        leftLength = leftDecArray.size
        for (i in decArray) {
            leftLength--
            result += i.toString().toInt(fromParse).toString(10).toBigDecimal() *
                    fromParse.toDouble().pow(leftLength.toDouble()).toBigDecimal()
        }
        while ((result.toString().length > 1 && result.toString()
                .contains("[,.]".toRegex()) && result.toString().endsWith("0")) || result.toString()
                .endsWith(".") || result.toString().endsWith(",")
        ) {
            result = result.toString().substring(0, result.toString().length - 1).toBigDecimal()
        }
        return result.toString()
    }

    private fun fromDec(): String {
        Log.d(APP_TAG, "Converter:: fromDec")

        var res = ""
        leftDecArray = string.split("[,.]".toRegex())[0].toCharArray()
        try {
            rightDecArray = string.split("[,.]".toRegex())[1].toCharArray()
        } catch (e: Exception) {
        }

        var left = ""
        for (i in leftDecArray) {
            left += i.toString()
        }
        try {
            res = left.toBigInteger(10).toString(toParse)
                .uppercase(Locale.getDefault()) //Перевод основной части числа в 10
        } catch (e: Exception) {
        }
        if (rightDecArray.isNotEmpty()) {
            var right = ""
            for (i in rightDecArray) {
                right += i.toString()
            }
            var z = 0
            var n1 = "0.$right"
            right = ""
            while (z < Shared.FRACTION_COUNT) {

                if (n1.contains("[,.]".toRegex())) {
                    n1 =
                        ("0.${n1.split("[,.]".toRegex())[1]}".toBigDecimal() * toParse.toBigDecimal()).toString() //Умножаем дробную часть на степень
                    if (!n1.contains("[,.]".toRegex())) break
                } else
                    break
                right += if (toParse < 11) {
                    n1.split("[,.]".toRegex())[0]  //Целую часть от каждой итерации приписываем к строке
                } else {
                    n1.split("[,.]".toRegex())[0].toBigInteger(10).toString(toParse)
                        .uppercase(Locale.getDefault()) //Целую часть от каждой итерации приписываем к строке
                }
                z++
            }
            res += ".$right"

            while ((res.length > 1 && res.contains("[,.]".toRegex()) && res.endsWith("0")) || res.endsWith(
                    "."
                ) || res.endsWith(",")
            ) {
                res = res.substring(0, res.length - 1)
            }
        }
        return res
    }

    private fun notDec(): String {
        Log.d(APP_TAG, "Converter:: notDec")

        string = toDec()
        fromParse = 10
        return fromDec()
    }
}