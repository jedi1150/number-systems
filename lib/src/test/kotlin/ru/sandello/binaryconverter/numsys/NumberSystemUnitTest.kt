package ru.sandello.binaryconverter.numsys

import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberSystemUnitTest {

    private val nsIntegerList = listOf(
        NumberSystem("10000000", Radix.BIN),
        NumberSystem("200", Radix.OCT),
        NumberSystem("128", Radix.DEC),
        NumberSystem("80", Radix.HEX),
        NumberSystem("3k", Radix(36)),
    )

    private val nsDecimalList = listOf(
        NumberSystem("10000000.011", Radix.BIN),
        NumberSystem("200.3", Radix.OCT),
        NumberSystem("128.375", Radix.DEC),
        NumberSystem("80.6", Radix.HEX),
        NumberSystem("3k.di", Radix(36)),
    )

    private val nsNegativeIntegerList = listOf(
        NumberSystem("-10000000", Radix.BIN),
        NumberSystem("-200", Radix.OCT),
        NumberSystem("-128", Radix.DEC),
        NumberSystem("-80", Radix.HEX),
        NumberSystem("-3k", Radix(36)),
    )

    private val nsNegativeDecimalList = listOf(
        NumberSystem("-10000000.011", Radix.BIN),
        NumberSystem("-200.3", Radix.OCT),
        NumberSystem("-128.375", Radix.DEC),
        NumberSystem("-80.6", Radix.HEX),
        NumberSystem("-3k.di", Radix(36)),
    )

    @Test
    fun convert_integerBetweenAllBases_preservesValue() {
        nsIntegerList.forEach { target ->
            nsIntegerList.forEach { source ->
                val result = NumSys.convert(source.value, source.radix.value, target.radix.value)
                assertEquals(target.value, result)
            }
        }
    }

    @Test
    fun convert_decimalBetweenAllBases_preservesValue() {
        nsDecimalList.forEach { target ->
            nsDecimalList.forEach { source ->
                val result = NumSys.convert(source.value, source.radix.value, target.radix.value)
                assertEquals(target.value, result)
            }
        }
    }

    @Test
    fun convert_negativeIntegerBetweenAllBases_preservesValue() {
        nsNegativeIntegerList.forEach { target ->
            nsNegativeIntegerList.forEach { source ->
                val result = NumSys.convert(source.value, source.radix.value, target.radix.value)
                assertEquals(target.value, result)
            }
        }
    }

    @Test
    fun convert_negativeDecimalBetweenAllBases_preservesValue() {
        nsNegativeDecimalList.forEach { target ->
            nsNegativeDecimalList.forEach { source ->
                val result = NumSys.convert(source.value, source.radix.value, target.radix.value)
                assertEquals(target.value, result)
            }
        }
    }

    @Test
    fun convert_zero_returnsZeroInAnyBase() {
        listOf(Radix.BIN, Radix.OCT, Radix.DEC, Radix.HEX).forEach { radix ->
            val result = NumSys.convert("0", radix.value, Radix.DEC.value)
            assertEquals("0", result)
        }
    }

    @Test
    fun convert_zeroWithFractional_returnsZeroInAnyBase() {
        listOf(Radix.BIN, Radix.OCT, Radix.DEC, Radix.HEX).forEach { radix ->
            val result = NumSys.convert("0.0", radix.value, Radix.DEC.value)
            assertEquals("0", result)
        }
    }
}
