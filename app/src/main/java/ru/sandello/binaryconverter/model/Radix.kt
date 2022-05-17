package ru.sandello.binaryconverter.model

import androidx.annotation.IntRange

@JvmInline
value class Radix(@IntRange(from = 2, to = 26) val value: Int)
