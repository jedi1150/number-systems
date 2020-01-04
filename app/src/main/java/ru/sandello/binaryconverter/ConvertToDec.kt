package ru.sandello.binaryconverter

import android.annotation.SuppressLint
import android.util.Log
import kotlin.math.pow

class ConvertTo {

    private var string = ""
    private var leftLength = 0 //Длина целой части числа
    private var result = 0.toBigDecimal() //Переменная суммы
    var returnString = "" //Возвращаемая строка
    var fromParse = 0
    var toParse = 0

    var decArray = charArrayOf()
    private var leftDecArray = charArrayOf()
    private var rightDecArray = charArrayOf()

    fun main(oldString: String, p1: Int?, p2: Int?): String {
        string = oldString
        fromParse = p1!!
        toParse = p2!!

        leftDecArray = oldString.split(".")[0].toCharArray()
        try {
            rightDecArray = oldString.split(".")[1].toCharArray()
        } catch (e: Exception) {
        }
        decArray = oldString.replace(".", "").toCharArray() //Строка без разделителя

        if (fromParse != 10 && toParse == 10) returnString = toDec()
        if (fromParse == 10 && toParse != 10) returnString = fromDec()
        if (fromParse == 10 && toParse == 10) returnString = fromDec()
        if (fromParse != 10 && toParse != 10) returnString = notDec()
        return returnString
    }

    private fun toDec(): String {
        leftLength = leftDecArray.size
        for (i in decArray) {
            leftLength--
            result += i.toString().toInt(fromParse).toString(10).toBigDecimal() *
                    fromParse.toDouble().pow(leftLength.toDouble()).toBigDecimal()
        }
        while ((result.toString().length > 1 && result.toString().contains(".") && result.toString().endsWith("0")) || result.toString().endsWith(".")) {
            result = result.toString().substring(0, result.toString().length - 1).toBigDecimal()
        }
        return result.toString()
    }

    @SuppressLint("DefaultLocale")
    private fun fromDec(): String {
        var res: String
        leftDecArray = string.split(".")[0].toCharArray()
        try {
            rightDecArray = string.split(".")[1].toCharArray()
        } catch (e: Exception) {
        }

        var left = ""
        for (i in leftDecArray) {
            left += i.toString()
        }
        res = left.toBigInteger(10).toString(toParse).toUpperCase() //Перевод основной части числа в 10
        if (rightDecArray.isNotEmpty()) {
            var right = ""
            for (i in rightDecArray) {
                right += i.toString()
            }
            var z = 0
            var n1 = "0.$right"
            right = ""
            while (z < fractionCount) {

                if (n1.contains(".")) {
                    n1 = ("0.${n1.split(".")[1]}".toBigDecimal() * toParse.toBigDecimal()).toString() //Умножаем дробную часть на степень
                    if (!n1.contains(".")) break
                } else
                    break
                right += if (toParse < 11) {
                    n1.split(".")[0]  //Целую часть от каждой итерации приписываем к строке
                } else {
                    n1.split(".")[0].toBigInteger(10).toString(toParse).toUpperCase() //Целую часть от каждой итерации приписываем к строке
                }
                z++
            }
            res += ".$right"

            while ((res.length > 1 && res.contains(".") && res.endsWith("0")) || res.endsWith(".")) {
                res = res.substring(0, res.length - 1)
            }
        }
        return res
    }

    private fun notDec(): String {
        string = toDec()
        fromParse = 10
        return fromDec()
    }
}