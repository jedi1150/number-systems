package ru.sandello.binaryconverter.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class OperandFormatter(private val radix: Int) : VisualTransformation {
    private val radixes = IntArray(36) { it + 1 }
    private val groupByThreeNumbers = radixes.filter { listOf(3, 7, 8, 9, 10, 11, 12, 13, 14, 15).contains(it) }
    private val groupByFourNumbers = radixes.filter { !listOf(3, 7, 8, 9, 10, 11, 12, 13, 14, 15).contains(it) }

    private fun groupLength(radix: Int): Int {
        return when (radix) {
            in groupByThreeNumbers -> 3
            in groupByFourNumbers -> 4
            else -> 3
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        // TODO Revert offset
        var out = ""
        for (i in text.text.indices) {
            if (i != 0 && i % groupLength(radix) == 0) out += " "
            out += text.text[i]
        }

        val operandOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + maxOf(0, offset - 1).div(groupLength(radix))
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset - maxOf(0, offset - 1).div(groupLength(radix) + 1)
            }
        }

        return TransformedText(AnnotatedString(out), operandOffsetTranslator)
    }
}