package com.example.robert_kraut_cz_8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import kotlin.math.exp

/**
 * @author Robert Kraut
 * podpunkty:
 * 1,2a,2bII,3,4,5,6,9
 */
class MainActivity : AppCompatActivity() {
    private lateinit var textEditor: TextEditor
    private lateinit var expressionParser: ExpressionParser
    private lateinit var operationsHandler: OperationsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textEditor = TextEditor(this)

        findViewById<TableLayout>(R.id.tableLayout).touchables
                .filterIsInstance<Button>()
                .forEach { it.setOnClickListener(this::handleButtonClick) }
    }

    fun handleButtonClick(view: View) {
        view as Button
        val textArea = findViewById<TextView>(R.id.editTextTextMultiLine)
        val text = view.text

        if (text == getString(R.string.textEq)) {
            textArea.editableText.clear()

            operationsHandler = OperationsHandlerImplementation(this)
            expressionParser = ExpressionParser(this, operationsHandler)

            expressionParser.parse(textEditor.toString())
            val result = operationsHandler.evaluateFormula()
            textArea.editableText.append(result.toString())
            textEditor.clear()
        } else {
            textEditor.currentText = text.toString()
            textArea.text = textEditor.toString()
        }
    }
}