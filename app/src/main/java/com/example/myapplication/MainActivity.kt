package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    //other
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ce : MaterialButton = findViewById(R.id.clearEntry)
        ce.setOnClickListener {
            entryClearAction()
        }

        val c : MaterialButton = findViewById(R.id.clearFormula)
        c.setOnClickListener {
            allClearAction()
        }

        val bs : MaterialButton = findViewById(R.id.backspace)
        bs.setOnClickListener {
            backSpaceAction()
        }

        val dv : MaterialButton = findViewById(R.id.devide)
        dv.setOnClickListener {
            operationAction(dv)
        }

        val mul : MaterialButton = findViewById(R.id.multiply)
        mul.setOnClickListener {
            operationAction(mul)
        }

        val pls : MaterialButton = findViewById(R.id.plus)
        pls.setOnClickListener {
            operationAction(pls)
        }

        val mns : MaterialButton = findViewById(R.id.minus)
        mns.setOnClickListener {
            operationAction(mns)
        }

        val nZero : MaterialButton = findViewById(R.id.zero)
        nZero.setOnClickListener {
            numberAction(nZero)
        }

        val nOne : MaterialButton = findViewById(R.id.one)
        nOne.setOnClickListener {
            numberAction(nOne)
        }

        val nTwo : MaterialButton = findViewById(R.id.two)
        nTwo.setOnClickListener {
            numberAction(nTwo)
        }

        val nThree : MaterialButton = findViewById(R.id.three)
        nThree.setOnClickListener {
            numberAction(nThree)
        }

        val nFour : MaterialButton = findViewById(R.id.four)
        nFour.setOnClickListener {
            numberAction(nFour)
        }

        val nFive : MaterialButton = findViewById(R.id.five)
        nFive.setOnClickListener {
            numberAction(nFive)
        }

        val nSix : MaterialButton = findViewById(R.id.six)
        nSix.setOnClickListener {
            numberAction(nSix)
        }

        val nSeven : MaterialButton = findViewById(R.id.seven)
        nSeven.setOnClickListener {
            numberAction(nSeven)
        }

        val nEight : MaterialButton = findViewById(R.id.eight)
        nEight.setOnClickListener {
            numberAction(nEight)
        }

        val nNine : MaterialButton = findViewById(R.id.nine)
        nNine.setOnClickListener {
            numberAction(nNine)
        }

        val dot : MaterialButton = findViewById(R.id.dot)
        dot.setOnClickListener {
            numberAction(dot)
        }

        val equals : MaterialButton = findViewById(R.id.equals)
        equals.setOnClickListener {
            equalsAction()
        }
    }

    private fun numberAction(view: View) {
        val formula: TextView = findViewById(R.id.tvFormula)

        if (view is MaterialButton) {

            if (view.text.equals(".")) {
                if (canAddDecimal) formula.append(view.text)
                canAddDecimal = false
            } else {
                formula.append(view.text)
                canAddOperation = true
            }
        }
    }

    private fun operationAction(view: View) {
        val formula: TextView = findViewById(R.id.tvFormula)

        if (view is MaterialButton && canAddOperation) {
            formula.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    private fun entryClearAction() {
        val result: TextView = findViewById(R.id.tvResult)

        result.text = "0"
    }

    private fun allClearAction() {
        val formula: TextView = findViewById(R.id.tvFormula)
        val result: TextView = findViewById(R.id.tvResult)

        formula.text = ""
        result.text = "0"
    }

    private fun backSpaceAction() {
        val formula: TextView = findViewById(R.id.tvFormula)
        val length = formula.length()

        if (length > 0) {
            formula.text = formula.text.subSequence(0, length - 1)
        }
    }

    private fun equalsAction() {
        val result: TextView = findViewById(R.id.tvResult)

        result.text = calculateResults()
    }

    private fun calculateResults() : String {
        val digitOperators = digitOperators()
        if(digitOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == "+")
                    result += nextDigit
                if (operator == "-")
                    result -= nextDigit
            }
        }

        return  result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>) : MutableList<Any> {
        var list = passedList
        while (list.contains("x") || list.contains("/")) {
            list = calcTimeDiv(list)
        }
        return list
    }

    private fun calcTimeDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()

        var restartIndex = passedList.size

        for(i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) newList.add(passedList[i])
        }

        return newList
    }

    private fun digitOperators() : MutableList<Any> {
        val formula: TextView = findViewById(R.id.tvFormula)
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in formula.text) {
            if (character.isDigit() || character == '.') currentDigit += character
            else {
                list.add(currentDigit)
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "") list.add(currentDigit.toFloat())

        return list
    }
}