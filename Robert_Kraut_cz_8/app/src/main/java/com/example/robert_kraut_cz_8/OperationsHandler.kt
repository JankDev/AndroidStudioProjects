package com.example.robert_kraut_cz_8

interface OperationsHandler {
    fun addNumber(num: Double)
    fun addOperation(op: String)
    fun evaluateFormula(): Double
}