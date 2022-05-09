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
        var out = ""

        val delimiterExists: Boolean = text.text.contains('.')
        val beforeDelimiter: String = text.text.substringBefore('.')
        val afterDelimiter: String = text.text.substringAfter('.', "")

        beforeDelimiter.indices.reversed().forEach { i ->
            out += beforeDelimiter.reversed()[i]
            if (i != 0 && i % groupLength == 0) {
                out += " "
            }
        }

        if (delimiterExists) out += "."

        afterDelimiter.run {
            if (isBlank()) return@run
            indices.reversed().forEach { i ->
                out += afterDelimiter.reversed()[i]
                if (i != 0 && i % groupLength == 0) {
                    out += " "
                }
            }
        }

        val operandOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val formattedBeforeDelimiter = out.substringBefore('.')
                val formattedAfterDelimiter = out.substringAfter('.', "")
                val delimiterLength = if (delimiterExists) 1 else 0
                val lastGroupBeforeDelimiterLength = formattedBeforeDelimiter.substringBefore(' ').length
                return when {
                    offset > 0 && offset <= beforeDelimiter.length -> {
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength) - 1).floorDiv(groupLength)
                    }
                    offset > 0 && offset == beforeDelimiter.length + delimiterLength -> {
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength - delimiterLength) - 1).floorDiv(groupLength)
                    }
                    offset > 0 && offset > beforeDelimiter.length + delimiterLength -> {
                        val lastGroupLength = formattedAfterDelimiter.substringBefore(' ').length
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength - delimiterLength - lastGroupLength) - 1).floorDiv(groupLength)
                    }
                    else -> {
                        offset
                    }
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val spacesBeforeOffset = if (out.contains(' ')) out.substring(0, offset).count { it == ' ' } else 0

                return offset - spacesBeforeOffset
            }
        }

        return TransformedText(AnnotatedString(out), operandOffsetTranslator)
    }
}