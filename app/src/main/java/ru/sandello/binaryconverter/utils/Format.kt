package ru.sandello.binaryconverter.utils

import android.widget.EditText

class Format {
    fun format(editText: EditText) {
        var string: String
        try {
            string = editText.text.toString()

            if (string.isNotEmpty()) {
                var pos = editText.selectionStart

                if (string.contains("-")) { //Правильно высставляем отрицание
                    if (string.toCharArray().count { it.toString().contains("-") } > 1 && pos > 0) {
                        string = string.replace("-", "")
                        string = string.replaceRange(0, 0, "-")
                        editText.setSelection(pos)
                        pos = editText.selectionStart - 1
                    } else {
                        string = string.replace("-", "")
                        string = string.replaceRange(0, 0, "-")
                        editText.setSelection(pos)
                    }
                }

                if (pos > 0 && (string.substring(pos - 1, pos)
                        .contains("[,.]".toRegex()))
                ) { //Убираем лишние разделители
                    string = string.replaceRange(pos - 1, pos, ".")
                    editText.setSelection(pos)
                }
                string = string.replaceFirst(".", ":")
                string = string.replace(".", "")
                string = string.replace(":", ".")

                if (string.substringAfter(".") != "0") {
                    if (pos > string.length) pos = string.length
                    editText.text.clear()
                    editText.append(string.uppercase())
                    editText.setSelection(pos)
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }
}