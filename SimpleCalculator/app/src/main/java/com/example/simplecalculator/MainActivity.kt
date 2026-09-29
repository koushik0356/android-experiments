package com.example.simplecalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextNumber2: EditText = findViewById(R.id.editTextNumber2)
        val editTextNumber3: EditText = findViewById(R.id.editTextNumber3)
        val editText3: EditText = findViewById(R.id.editText3)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)

        button1.setOnClickListener {
            editText3.setText(operate(
                num1Str = editTextNumber2.text.toString(),
                num2Str = editTextNumber3.text.toString(),
                operation = button1.text.toString()
            ))
        }

        button2.setOnClickListener {
            editText3.setText(operate(
                num1Str = editTextNumber2.text.toString(),
                num2Str = editTextNumber3.text.toString(),
                operation = button2.text.toString()
            ))
        }

        button3.setOnClickListener {
            editText3.setText(operate(
                num1Str = editTextNumber2.text.toString(),
                num2Str = editTextNumber3.text.toString(),
                operation = button3.text.toString()
            ))
        }

        button4.setOnClickListener {
            editText3.setText(operate(
                num1Str = editTextNumber2.text.toString(),
                num2Str = editTextNumber3.text.toString(),
                operation = button4.text.toString()
            ))
        }

        Log.d("LifeCycleDemo", "onCreate() Method Called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycleDemo", "onStart() Method Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycleDemo", "onResume() Method called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycleDemo", "onPause() Method called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycleDemo", "onStop() Method called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycleDemo", "onDestroy() Method called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LifeCycleDemo", "onRestart() Method called")
    }

    private fun operate(num1Str: String, num2Str: String, operation: String): String {
        // toDoubleOrNull() will safely return a null instead of crashing if the input is empty or invalid
        val n1 = num1Str.toDoubleOrNull()
        val n2 = num2Str.toDoubleOrNull()

        if (n1 != null && n2 != null) {
            if (n2 == 0.0 && operation == "/") {
                Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
                return "Cannot divide by zero"
            }

            return when (operation) {
                "+" -> (n1 + n2).toString()
                "-" -> (n1 - n2).toString()
                "*" -> (n1 * n2).toString()
                "/" -> (n1 / n2).toString()
                else -> "Invalid operation"
            }
        } else {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            return "Please enter both numbers"
        }
    }
}