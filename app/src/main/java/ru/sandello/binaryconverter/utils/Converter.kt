package ru.sandello.binaryconverter.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.sandello.binaryconverter.model.ConvertedData
import java.util.*
import kotlin.math.pow

class Converter {

    operator fun invoke(value: String, fromRadix: Int, toRadix: Int): Flow<ConvertedData> = flow {
        Log.d(APP_TAG, "Converter::invoke")

        assert(fromRadix > 2 || fromRadix < 36 || toRadix > 2 || fromRadix < 36) {
            "Radix must be greater than 2 and smaller than 36"
        }

        if (fromRadix == toRadix) {
            emit(ConvertedData(result = value, fromRadix = fromRadix, toRadix = toRadix))
            return@flow
        }

        emit(ConvertedData(result = convert(value, fromRadix, toRadix), fromRadix = fromRadix, toRadix = toRadix))
    }.flowOn(Dispatchers.Default)

    private fun convert(value: String, fromRadix: Int, toRadix: Int): String {
        Log.d(APP_TAG, "Converter::convert: from radix $fromRadix to radix $toRadix")

        var string = value

        var minusBool = false
        if (string.contains("-")) {
            string = string.replace("-", "")
            minusBool = true
        }

        var result: String

        if (fromRadix != 10) {
            result = toDec(string, fromRadix)
            if (toRadix != 10) {
                result = fromDec(result, toRadix)
            }
        } else {
            result = fromDec(string, toRadix)
        }

        if (minusBool) {
            result = result.replaceRange(0, 0, "-")
        }
        return result
    }

    private fun toDec(value: String, fromRadix: Int): String {
        Log.d(APP_TAG, "Converter::toDec")

        val valueWithoutComma = value.replace("[,.]".toRegex(), "")
        val integerPart = value.split("[,.]".toRegex())[0]

        val dec = valueWithoutComma.toCharArray().mapIndexed { index, char ->
            char.toString().toInt(fromRadix).toString(10).toBigDecimal() * fromRadix.toDouble().pow(integerPart.toCharArray().size - (index + 1)).toBigDecimal()
        }
        var result = dec.reduce { acc, decimal -> acc + decimal }.toString()    // Summing all chars

        // Pretty formatting
        while (result.length > 1 && result.contains("[,.]".toRegex()) && result.endsWith("0")) {
            result = result.split("[,.]".toRegex())[0]
        }

        return result
    }

    private fun fromDec(value: String, toRadix: Int): String {
        Log.d(APP_TAG, "Converter::fromDec")

        var result: String = value.split("[,.]".toRegex())[0].toBigInteger(10).toString(toRadix).uppercase(Locale.getDefault())
        if ((value.contains(",") || value.contains(".")) && value.split("[,.]".toRegex())[1].isNotEmpty()) {
            var fractionalPart = value
            var i = 0
            var convertedFraction = ""
            while (i < Shared.FRACTION_COUNT) {
                if (fractionalPart.split("[,.]".toRegex())[1].toBigDecimal() == "0".toBigDecimal()) break
                fractionalPart = (".${fractionalPart.split("[,.]".toRegex())[1]}".toBigDecimal() * toRadix.toBigDecimal()).toString() //Умножаем дробную часть на степень
                i++
                convertedFraction += fractionalPart.split("[,.]".toRegex())[0].toBigInteger(10).toString(toRadix).uppercase(Locale.getDefault())
            }
            result += ".$convertedFraction"
        }
        return result
    }

}
