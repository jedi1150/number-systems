package ru.sandello.binaryconverter.model

import java.math.BigDecimal


data class Division(
    val dividend: BigDecimal,
    val divisor: Int,
    val quotient: BigDecimal,
    val remainder: BigDecimal,
    val convertedRemainder: String? = null,
)