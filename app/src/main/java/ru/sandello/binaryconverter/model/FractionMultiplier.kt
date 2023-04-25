package ru.sandello.binaryconverter.model

data class FractionMultiplier(
    val multiplier: String,
    val multiplicand: Int,
    val product: String,
    val convertedProduct: String? = null,
)
