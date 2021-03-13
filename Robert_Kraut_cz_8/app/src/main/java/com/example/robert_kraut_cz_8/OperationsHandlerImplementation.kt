package com.example.robert_kraut_cz_8

import android.content.Context
import java.util.*

typealias Operation = String

class OperationsHandlerImplementation(
        private val context: Context,
        private val numbers: MutableList<Double> = mutableListOf(),
        private val operations: MutableList<Operation> = mutableListOf()
) : OperationsHandler {
    override fun addNumber(num: Double) {
        numbers.add(num)
    }

    override fun addOperation(op: String) {
        operations.add(op)
    }

    override fun evaluateFormula(): Double {
        if (numbers.isEmpty()) return 0.0
        val rpn = convertToRPN(LinkedList(numbers), LinkedList(operations))

        return evalRPN(rpn)
    }

    fun convertToRPN(numbers: Queue<Double>, operations: Queue<String>): MutableList<String> {
        val result = mutableListOf<String>()

        val stack: Stack<String> = Stack()
        while (numbers.isNotEmpty()) {
            val number = numbers.poll()!!
            val sign = operations.poll()
            result.add(number.toString())
            if (sign != null) {
                while (!stack.isEmpty() && getPriority(stack.peek()) >= getPriority(sign)) {
                    result.add(stack.pop())
                }
            }
            if (sign != null) {
                stack.push(sign)
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop())
        }
        return result
    }

    private fun getPriority(op: String) = if (op == context.getString(R.string.textPlus) || op == context.getString(R.string.textMinus)) 0 else 1

    fun evalRPN(tokens: MutableList<String>): Double {
        val stack = Stack<Double>()
        tokens.forEach { token ->
            if (isOperator(token)) {
                stack.push(compute(token, stack.pop(), stack.pop()))
            } else {
                stack.push(token.toDouble())
            }
        }
        return stack.pop()
    }

    private fun isOperator(token: String) = token == context.getString(R.string.textPlus) || token == context.getString(R.string.textMinus)
            || token == context.getString(R.string.textMul) || token == context.getString(R.string.textDiv)

    private fun compute(operator: String, num2: Double, num1: Double): Double {
        var result = 0.0
        when (operator[0].toString()) {
            context.getString(R.string.textPlus) -> result = num1 + num2
            context.getString(R.string.textMinus) -> result = num1 - num2
            context.getString(R.string.textMul) -> result = num1 * num2
            context.getString(R.string.textDiv) -> result = num1 / num2
        }
        return result
    }

}