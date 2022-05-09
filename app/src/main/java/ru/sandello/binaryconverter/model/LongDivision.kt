package ru.sandello.binaryconverter.model

import java.math.BigDecimal


data class LongDivision(
    val dividend: BigDecimal,
    val divisor: BigDecimal,
    val quotient: BigDecimal,
    val division: BigDecimal,
    val remainder: BigDecimal,
)