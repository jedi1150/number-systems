package ru.sandello.binaryconverter.model

import android.annotation.SuppressLint
import androidx.annotation.IntRange

@JvmInline
value class Radix(@IntRange(from = 2, to = 26) val value: Int)

@SuppressLint("Range")
private val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }
private val groupByThreeNumbers = radixes.filter { listOf(Radix(3), Radix(7), Radix(8), Radix(9), Radix(10), Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }
private val groupByFourNumbers = radixes.filter { !listOf(Radix(3), Radix(7), Radix(8), Radix(9), Radix(10), Radix(11), Radix(12), Radix(13), Radix(14), Radix(15)).contains(it) }

fun Radix.groupLength(): Int {
    return when (this) {
        in groupByThreeNumbers -> 3
        in groupByFourNumbers -> 4
        else -> 3
    }
}

fun NumberSystem.groupLength(): Int = this.radix.groupLength()