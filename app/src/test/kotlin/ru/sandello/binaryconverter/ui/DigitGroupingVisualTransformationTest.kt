package ru.sandello.binaryconverter.ui

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.pretty
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.utils.NS_GROUP_SEPARATOR

class DigitGroupingVisualTransformationTest {
    @Test
    fun filter_decimalInteger_insertsGroupSeparators() {
        val transformed = DigitGroupingVisualTransformation(Radix.DEC)
            .filter(AnnotatedString("1234567"))

        assertEquals("1${NS_GROUP_SEPARATOR}234${NS_GROUP_SEPARATOR}567", transformed.text.text)
    }

    @Test
    fun filter_binaryValue_groupsByFour() {
        val transformed = DigitGroupingVisualTransformation(Radix.BIN)
            .filter(AnnotatedString("11111111"))

        assertEquals("1111${NS_GROUP_SEPARATOR}1111", transformed.text.text)
    }

    @Test
    fun pretty_respectsDigitGroupingFlag() {
        val number = NumberSystem("1234567", Radix.DEC)

        assertEquals("1${NS_GROUP_SEPARATOR}234${NS_GROUP_SEPARATOR}567", number.pretty(true))
        assertEquals("1234567", number.pretty(false))
    }
}
