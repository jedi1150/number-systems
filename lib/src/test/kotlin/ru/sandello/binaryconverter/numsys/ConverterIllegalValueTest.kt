package ru.sandello.binaryconverter.numsys

import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class ConverterIllegalValueTest {

    private val targetRadixes = listOf(Radix.BIN, Radix.OCT, Radix.DEC, Radix.HEX)

    @Test
    fun convert_emptyValue_throwsIllegalArgumentException() {
        val ns = NumberSystem("", Radix.BIN)
        targetRadixes.forEach { target ->
            assertFailsWith(IllegalArgumentException::class) {
                NumSys.convert(numberSystem = ns, targetRadix = target)
            }
        }
    }

    @Test
    fun convert_blankValue_throwsIllegalArgumentException() {
        val ns = NumberSystem("    ", Radix.OCT)
        targetRadixes.forEach { target ->
            assertFailsWith(IllegalArgumentException::class) {
                NumSys.convert(numberSystem = ns, targetRadix = target)
            }
        }
    }

    @Test
    fun convert_invalidCharacters_throwsIllegalArgumentException() {
        val ns = NumberSystem("-,. ", Radix.HEX)
        targetRadixes.forEach { target ->
            assertFailsWith(IllegalArgumentException::class) {
                NumSys.convert(numberSystem = ns, targetRadix = target)
            }
        }
    }

    @Test
    fun convertString_emptyValue_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("", Radix.DEC.value, Radix.BIN.value)
        }
    }

    @Test
    fun convertString_blankValue_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("   ", Radix.DEC.value, Radix.BIN.value)
        }
    }

    @Test
    fun convertString_invalidCharacters_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("12,34", Radix.DEC.value, Radix.HEX.value)
        }
    }

    @Test
    fun convertString_sourceRadixOutOfRange_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("10", 1, Radix.DEC.value)
        }
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("10", 63, Radix.DEC.value)
        }
    }

    @Test
    fun convertString_targetRadixOutOfRange_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("10", Radix.DEC.value, 1)
        }
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("10", Radix.DEC.value, 63)
        }
    }

    @Test
    fun convert_digitOutOfRangeForRadix_throwsIllegalArgumentException() {
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("2", Radix.BIN.value, Radix.DEC.value) // "2" is invalid in binary
        }
        assertFailsWith(IllegalArgumentException::class) {
            NumSys.convert("8", Radix.OCT.value, Radix.DEC.value) // "8" is invalid in octal
        }
    }
}
