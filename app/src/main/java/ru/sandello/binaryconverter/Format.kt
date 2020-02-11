package ru.sandello.binaryconverter

import android.util.Log
import android.widget.EditText
import java.util.*

class Format {
    fun format(editText: EditText) {
        var string: String
        try {
            string = editText.text.toString()

            if (string.isNotEmpty()) {
                var pos = editText.selectionStart
                if (string.contains("-")) { //Правильно высставляем отрицание
                    Log.d("string", string.indexOf("-").toString())
                    editText.setText(string.replace("-", ""))
                    string = string.replace("-", "")
                    string = string.replaceRange(0, 0, "-")
                    if (string.count { it.toString().contains("-") } > 1 && pos > 0) {
                        editText.setSelection(pos - 1)
                        pos = editText.selectionStart - 1
                    }
                    else {
                        editText.setSelection(pos)
                        pos = editText.selectionStart
                    }
                }

                if (pos > 0 && (string.substring(pos - 1, pos).contains("[,.]".toRegex()))) { //Убираем лишние разделители
                    editText.setText(string.replaceRange(pos - 1, pos, "."))
                    string = string.replaceRange(pos - 1, pos, ".")
                    editText.setSelection(pos)
                }
                string = string.replaceFirst(".", ":")
                string = string.replace(".", "")
                string = string.replace(":", ".")

                if (string.substringAfter(".") != "0") {
                    editText.setText(string.toUpperCase(Locale.getDefault()))
                    editText.setSelection(pos)
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }
}