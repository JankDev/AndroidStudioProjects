package com.example.robert_kraut_cz_8

import android.content.Context

class ExpressionParser(private val context: Context,
                       private val operationsHandler: OperationsHandler) {
    fun parse(expression: String) {
        var trimmedExpression = expression.trim().trimEnd { !it.isDigit() }

        while (trimmedExpression.isNotEmpty()) {
            val firstSignIndex = trimmedExpression.indexOfFirst { !it.isDigit() && it.toString() != context.getString(R.string.textDot) }
            trimmedExpression = if (firstSignIndex > 0) {
                operationsHandler.addNumber(trimmedExpression.subSequence(0, firstSignIndex).toString().toDouble())
                operationsHandler.addOperation(trimmedExpression[firstSignIndex].toString())
                trimmedExpression.substring(firstSignIndex + 1)
            } else {
                if (firstSignIndex == 0) {
                    val index = trimmedExpression.substring(1).indexOfFirst { !it.isDigit() && it.toString() != context.getString(R.string.textDot) }
                    if (index > 0) {
                        operationsHandler.addNumber(-trimmedExpression.subSequence(1, index + 1).toString().toDouble())
                        operationsHandler.addOperation(trimmedExpression[index + 1].toString())
                        trimmedExpression.substring(index + 2)

                    } else {
                        operationsHandler.addNumber(trimmedExpression.toDouble())
                        ""
                    }

                } else {
                    operationsHandler.addNumber(trimmedExpression.toDouble())
                    ""
                }
            }
        }
    }
}