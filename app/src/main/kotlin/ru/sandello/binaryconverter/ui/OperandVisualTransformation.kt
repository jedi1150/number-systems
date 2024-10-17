package ru.sandello.binaryconverter.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.numsys.model.groupLength
import ru.sandello.binaryconverter.utils.COMMA
import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.NS_GROUP_SEPARATOR

class OperandVisualTransformation(radix: Radix) : VisualTransformation {
    private val groupLength: Int = radix.groupLength()

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""

        val delimiterExists: Boolean = text.text.replace(COMMA, NS_DELIMITER).contains(NS_DELIMITER)
        val beforeDelimiter: String = text.text.replace(COMMA, NS_DELIMITER).substringBefore(NS_DELIMITER)
        val afterDelimiter: String = text.text.replace(COMMA, NS_DELIMITER).substringAfter(NS_DELIMITER, "")

        beforeDelimiter.indices.reversed().forEach { i ->
            out += beforeDelimiter.reversed()[i]
            if (i != 0 && i % groupLength == 0) {
                out += NS_GROUP_SEPARATOR
            }
        }

        if (delimiterExists) out += NS_DELIMITER

        afterDelimiter.run {
            if (isBlank()) return@run
            indices.reversed().forEach { i ->
                out += afterDelimiter.reversed()[i]
                if (i != 0 && i % groupLength == 0) {
                    out += NS_GROUP_SEPARATOR
                }
            }
        }

        val operandOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val formattedBeforeDelimiter = out.substringBefore(NS_DELIMITER)
                val formattedAfterDelimiter = out.substringAfter(NS_DELIMITER, "")
                val delimiterLength = if (delimiterExists) 1 else 0
                val lastGroupBeforeDelimiterLength = formattedBeforeDelimiter.substringBefore(NS_GROUP_SEPARATOR).length
                return when {
                    offset > 0 && offset <= beforeDelimiter.length -> {
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength) - 1).floorDiv(groupLength)
                    }
                    offset > 0 && offset == beforeDelimiter.length + delimiterLength -> {
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength - delimiterLength) - 1).floorDiv(groupLength)
                    }
                    offset > 0 && offset > beforeDelimiter.length + delimiterLength -> {
                        val lastGroupLength = formattedAfterDelimiter.substringBefore(NS_GROUP_SEPARATOR).length
                        offset + (offset + (groupLength - lastGroupBeforeDelimiterLength - delimiterLength - lastGroupLength) - 1).floorDiv(groupLength)
                    }
                    else -> {
                        offset
                    }
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val spacesBeforeOffset = if (out.contains(NS_GROUP_SEPARATOR)) out.substring(0, offset).count { it == NS_GROUP_SEPARATOR } else 0

                return offset - spacesBeforeOffset
            }
        }

        return TransformedText(AnnotatedString(out), operandOffsetTranslator)
    }
}