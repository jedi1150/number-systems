package ru.sandello.binaryconverter

import android.text.InputType
import android.widget.EditText


class TypeMethod {
    fun type(editText: EditText, allowVal: String?) {
        if (allowVal!!.length <= 10) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        else
        {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }
    }
}