package ru.sandello.binaryconverter.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class OperandVisualTransformation(radix: Int) : VisualTransformation {
    private val groupLength: Int
    private val radixes = IntArray(36) { radix -> radix + 1 }.filter { !listOf(1).contains(it) }
    private val groupByThreeNumbers = radixes.filter { listOf(3, 7, 8, 9, 10, 11, 12, 13, 14, 15).contains(it) }
    private val groupByFourNumbers = radixes.filter { !listOf(3, 7, 8, 9, 10, 11, 12, 13, 14, 15).contains(it) }

    init {
        groupLength = groupLength(radix)
    }

    private fun groupLength(radix: Int): Int {
        return when (radix) {
            in groupByThreeNumbers -> 3
            in groupByFourNumbers -> 4
            else -> 3
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        // TODO Add delimiter processing
        var out = ""
        text.text.indices.reversed().forEach { i ->
            out += text.text.reversed()[i]
            if (i != 0 && i % groupLength == 0) {
                out += " "
            }
        }

        val operandOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val lastGroupLength = out.substringBefore(' ').length
                return offset + maxOf(0, offset + (groupLength - lastGroupLength) - 1).floorDiv(groupLength)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val spacesBeforeOffset = if (out.contains(' ')) out.substring(0, offset).count { it == ' ' } else 0

                return offset - spacesBeforeOffset
            }
        }

        return TransformedText(AnnotatedString(out), operandOffsetTranslator)
    }
}