package ru.sandello.binaryconverter.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.sandello.binaryconverter.model.ConvertedData
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import java.util.*
import javax.inject.Inject
import kotlin.math.pow

class Converter @Inject constructor() {

    operator fun invoke(from: NumberSystem, toRadix: Radix): Flow<ConvertedData> = flow {
        Log.d(APP_TAG, "Converter::invoke")

        assert(from.radix.value > 2 || from.radix.value < 36 || toRadix.value > 2 || from.radix.value < 36) {
            "Radix must be greater than 2 and smaller than 36"
        }

        if (from.radix == toRadix) {
            emit(ConvertedData(fromRadix = from.radix, result = NumberSystem(value = from.value, radix = from.radix)))
            return@flow
        }

        emit(ConvertedData(fromRadix = from.radix, result = convert(from, toRadix)))
    }.flowOn(Dispatchers.Default)

    private fun convert(fromNumberSystem: NumberSystem, toRadix: Radix): NumberSystem {
        Log.d(APP_TAG, "Converter::convert: from radix ${fromNumberSystem.radix.value} to radix ${toRadix.value}")

        var minusBool = false
        if (fromNumberSystem.value.contains("-")) {
            fromNumberSystem.value = fromNumberSystem.value.replace("-", "")
            minusBool = true
        }

        var result: NumberSystem

        if (fromNumberSystem.radix != Radix.DEC) {
            result = toDec(fromNumberSystem)
            if (toRadix != Radix.DEC) {
                result = fromDec(result, toRadix)
            }
        } else {
            result = fromDec(fromNumberSystem, toRadix)
        }

        if (minusBool) {
            result.value = result.value.replaceRange(0, 0, "-")
        }
        return result
    }

    private fun toDec(from: NumberSystem): NumberSystem {
        Log.d(APP_TAG, "Converter::toDec")

        val valueWithoutComma = from.value.replace("[,.]".toRegex(), "")
        val integerPart = from.value.split("[,.]".toRegex())[0]

        val dec = valueWithoutComma.toCharArray().mapIndexed { index, char ->
            char.toString().toInt(from.radix.value).toString(10).toBigDecimal() * from.radix.value.toDouble().pow(integerPart.toCharArray().size - (index + 1)).toBigDecimal()
        }
        var result = dec.reduce { acc, decimal -> acc + decimal }.toString()    // Summing all chars

        // Pretty formatting
        while (result.length > 1 && result.contains("[,.]".toRegex()) && result.endsWith("0")) {
            result = result.substringBeforeLast("0")
        }

        while (result.length > 1 && result.contains("[,.]".toRegex()) && (result.endsWith(COMMA) || result.endsWith(NS_DELIMITER))) {
            result = result.split("[,.]".toRegex())[0]
        }

        return NumberSystem(value = result, radix = Radix.DEC)
    }

    private fun fromDec(value: NumberSystem, toRadix: Radix): NumberSystem {
        Log.d(APP_TAG, "Converter::fromDec")

        var result: String = value.value.split("[,.]".toRegex())[0].toBigInteger(10).toString(toRadix.value).uppercase(Locale.getDefault())
        if ((value.value.contains(COMMA) || value.value.contains(NS_DELIMITER)) && value.value.split("[,.]".toRegex())[1].isNotEmpty()) {
            var fractionalPart = value.value
            var i = 0
            var convertedFraction = ""
            while (i < Shared.FRACTIONAL_LENGTH) {
                if (fractionalPart.split("[,.]".toRegex())[1].toBigDecimal() == "0".toBigDecimal()) break
                fractionalPart = (".${fractionalPart.split("[,.]".toRegex())[1]}".toBigDecimal() * toRadix.value.toBigDecimal()).toString()
                i++
                convertedFraction += fractionalPart.split("[,.]".toRegex())[0].toBigInteger(10).toString(toRadix.value).uppercase(Locale.getDefault())
            }
            result += ".$convertedFraction"
        }

        // Pretty formatting
        while (result.length > 1 && result.contains("[,.]".toRegex()) && result.endsWith("0")) {
            result = result.substringBeforeLast("0")
        }

        return NumberSystem(value = result, radix = toRadix)
    }

}
