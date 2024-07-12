package numsys

import numsys.model.NumberSystem
import numsys.model.Radix
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberSystemUnitTest {

    private val nsIntegerList = listOf(
        NumberSystem("10000000", Radix.BIN),
        NumberSystem("200", Radix.OCT),
        NumberSystem("128", Radix.DEC),
        NumberSystem("80", Radix.HEX),
        NumberSystem("3k", Radix(36)),
        NumberSystem("2c", Radix(58)),
    )

    private val nsDecimalList = listOf(
        NumberSystem("10000000.011", Radix.BIN),
        NumberSystem("200.3", Radix.OCT),
        NumberSystem("128.375", Radix.DEC),
        NumberSystem("80.6", Radix.HEX),
        NumberSystem("3k.di", Radix(36)),
        NumberSystem("2c.lHt", Radix(58)),
    )

    @Test
    fun ns_integer_checkAll() {
        nsIntegerList.forEach { ns ->
            nsIntegerList.forEach { ns2 ->
                val result = NumSys.convert(ns2.value, ns2.radix.value, ns.radix.value)
                println(result)
                assertEquals(ns.value, result)
            }
        }
    }

    @Test
    fun ns_decimal_checkAll() {
        nsDecimalList.forEach { ns ->
            nsDecimalList.forEach { ns2 ->
                val result = NumSys.convert(ns2.value, ns2.radix.value, ns.radix.value)
                println(result)
                assertEquals(ns.value, result)
            }
        }
    }

}
