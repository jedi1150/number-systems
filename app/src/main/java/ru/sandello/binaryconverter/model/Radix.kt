package ru.sandello.binaryconverter.model

import android.annotation.SuppressLint
import androidx.annotation.IntRange

@JvmInline
value class Radix(@IntRange(from = 2, to = 36) val value: Int) {
    companion object {
        val BIN = Radix(2)
        val OCT = Radix(8)
        val DEC = Radix(10)
        val HEX = Radix(16)
    }
}

@SuppressLint("Range")
private val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !arrayOf(Radix(1)).contains(radix) }
private val groupByThreeNumbers: List<Radix> = radixes.filter { listOf(Radix(3), Radix(7), Radix.OCT, Radix(9), Radix.DEC, Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }
private val groupByFourNumbers: List<Radix> = radixes.filter { !listOf(Radix(3), Radix(7), Radix.OCT, Radix(9), Radix.DEC, Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }

fun Radix.groupLength(): Int {
    return when (this) {
        in groupByThreeNumbers -> 3
        in groupByFourNumbers -> 4
        else -> 3
    }
}

fun NumberSystem.groupLength(): Int = this.radix.groupLength()
