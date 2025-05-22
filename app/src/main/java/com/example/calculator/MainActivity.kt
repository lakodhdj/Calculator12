package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentInput = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var isNewOperation = true
    private var isNegative = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        )
        numberButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { appendNumber((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { handleMinus() }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("/") }
        findViewById<Button>(R.id.btnPower).setOnClickListener { setOperator("^") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { setOperator("%") }

        findViewById<Button>(R.id.btnSqrt).setOnClickListener { calculateFunction("sqrt") }
        findViewById<Button>(R.id.btnSin).setOnClickListener { calculateFunction("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { calculateFunction("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { calculateFunction("tan") }
        findViewById<Button>(R.id.btnLn).setOnClickListener { calculateFunction("ln") }

        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculate() }
    }

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            currentInput = ""
            isNewOperation = false
        }
        if (number == "." && currentInput.contains(".")) return
        currentInput += number
        updateDisplay()
    }

    private fun handleMinus() {
        if (currentInput.isEmpty()) {
            isNegative = !isNegative
            updateDisplay()
        } else {
            firstNumber = getCurrentNumber()
            operator = "-"
            isNewOperation = true
            isNegative = false
            updateDisplay()
        }
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            firstNumber = getCurrentNumber()
            operator = op
            isNewOperation = true
            isNegative = false
        }
    }

    private fun calculateFunction(func: String) {
        if (currentInput.isNotEmpty()) {
            val number = getCurrentNumber()
            val result = when (func) {
                "sqrt" -> if (number >= 0) sqrt(number) else "Ошибка"
                "sin" -> sin(Math.toRadians(number))
                "cos" -> cos(Math.toRadians(number))
                "tan" -> tan(Math.toRadians(number))
                "ln" -> if (number > 0) ln(number) else "Ошибка"
                else -> number
            }
            currentInput = if (result is String) result else result.toString()
            updateDisplay()
            isNewOperation = true
            isNegative = false
        }
    }

    private fun calculate() {
        if (currentInput.isNotEmpty() && operator.isNotEmpty()) {
            val secondNumber = getCurrentNumber()
            val result = when (operator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else "Ошибка"
                "^" -> firstNumber.pow(secondNumber)
                "%" -> firstNumber + (firstNumber * secondNumber / 100.0)
                else -> 0.0
            }
            currentInput = if (result is String) result else result.toString()
            updateDisplay()
            operator = ""
            isNewOperation = true
            isNegative = false
        }
    }

    private fun clear() {
        currentInput = ""
        operator = ""
        firstNumber = 0.0
        isNewOperation = true
        isNegative = false
        updateDisplay()
    }

    private fun updateDisplay() {
        display.text = when {
            currentInput.isEmpty() && isNegative -> "-"
            currentInput.isEmpty() -> "0"
            isNegative -> "-$currentInput"
            else -> currentInput
        }
    }

    private fun getCurrentNumber(): Double {
        return (if (isNegative && currentInput.isNotEmpty()) "-$currentInput" else currentInput).toDoubleOrNull() ?: 0.0
    }
}