package com.example.robert_kraut_cz_8

import android.content.Context


class TextEditor(private val context: Context) {
    private var numberMode = false
    var currentText: String = ""
        set(value) {
            field = when (value) {
                context.getString(R.string.textDot) ->
                    if (field.isEmpty()) {
                        numberMode = true
                        "0."
                    } else if (numberMode) {
                        field
                    } else {
                        numberMode = true
                        if (field.last().isDigit()) "$field."
                        else field + "0."
                    }
                context.getString(R.string.textMinus) -> {
                    numberMode = false
                    if (field.isNotEmpty() && (field.last().toString() == context.getString(R.string.textMinus) || field.last().toString() == context.getString(R.string.textDot))) field
                    else field + value
                }
                context.getString(R.string.textDiv),
                context.getString(R.string.textPlus),
                context.getString(R.string.textMul) -> {
                    numberMode = false
                    if (field.isEmpty() || !field.last().isDigit() || field.last().toString() == context.getString(R.string.textDot)) field
                    else {
                        if (field.last().toString() == context.getString(R.string.textDot)) field + "0$value"
                        else field + value
                    }
                }
                context.getString(R.string.textClear) -> ""
                "" -> ""
                context.getString(R.string.text0) -> {
                    if (field.isNotEmpty() && field.last().toString() == context.getString(R.string.text0) && !numberMode) field
                    else field + value
                }
                else -> {
                    if (field.isNotEmpty() && field.last().toString() == context.getString(R.string.text0) && !field[field.length - 2].isDigit() &&
                            field[field.length - 2].toString() != context.getString(R.string.textDot)) StringBuilder(field).also { it.setCharAt(it.length - 1, value[0]) }.toString()
                    else field + value
                }
            }
        }

    override fun toString(): String {
        return currentText
    }

    fun clear() {
        currentText = ""
    }
}