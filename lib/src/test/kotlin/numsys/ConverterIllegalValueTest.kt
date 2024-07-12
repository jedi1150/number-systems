package numsys

import numsys.model.NumberSystem
import numsys.model.Radix
import org.junit.Test
import kotlin.test.assertFailsWith

class ConverterIllegalValueTest {
    private val emptyValue = ""
    private val blankValue = "    "
    private val dotValue = "."
    private val commaValue = ","
    private val dotCommaValue = ".,"
    private val mixedValue = "-,. "

    private val nsBin = NumberSystem(emptyValue, Radix.BIN)
    private val nsOct = NumberSystem(blankValue, Radix.OCT)
    private val nsDec = NumberSystem(dotValue, Radix.DEC)
    private val nsHex = NumberSystem(mixedValue, Radix.HEX)

    @Test
    fun binToBinTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsBin, targetRadix = Radix.BIN) }
    }

    @Test
    fun binToOctTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsBin, targetRadix = Radix.OCT) }
    }

    @Test
    fun binToDecTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsBin, targetRadix = Radix.DEC) }
    }

    @Test
    fun binToHexTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsBin, targetRadix = Radix.HEX) }
    }

    @Test
    fun octToBinTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsOct, targetRadix = Radix.BIN) }
    }

    @Test
    fun octToOctTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsOct, targetRadix = Radix.OCT) }
    }

    @Test
    fun octToDecTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsOct, targetRadix = Radix.DEC) }
    }

    @Test
    fun octToHexTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsOct, targetRadix = Radix.HEX) }
    }

    @Test
    fun hexToBinTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsHex, targetRadix = Radix.BIN) }
    }

    @Test
    fun hexToOctTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsHex, targetRadix = Radix.OCT) }
    }

    @Test
    fun hexToDecTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsHex, targetRadix = Radix.DEC) }
    }

    @Test
    fun hexToHexTest() {
        assertFailsWith(IllegalArgumentException::class) { NumSys.convert(numberSystem = nsHex, targetRadix = Radix.HEX) }
    }

}
