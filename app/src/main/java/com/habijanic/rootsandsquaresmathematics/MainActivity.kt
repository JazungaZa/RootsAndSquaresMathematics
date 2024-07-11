package com.habijanic.rootsandsquaresmathematics

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var numUpTo : EditText
    lateinit var square : Button
    lateinit var root : Button
    var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        numUpTo = findViewById(R.id.editTextNumber)
        square = findViewById(R.id.buttonSquares)
        root = findViewById(R.id.buttonRoots)

        square.setOnClickListener {

            val input = numUpTo.text.toString()
            if (input.isNullOrEmpty()){
                Toast.makeText(applicationContext,"enter a number",Toast.LENGTH_LONG).show()
            }
            else{
                number = Integer.parseInt(input)
                Toast.makeText(applicationContext,"$number",Toast.LENGTH_LONG).show()
            }

        }
        root.setOnClickListener {

            val input = numUpTo.text.toString()
            if (input.isNullOrEmpty()){
                Toast.makeText(applicationContext,"enter a number",Toast.LENGTH_LONG).show()
            }
            else{
                number = Integer.parseInt(input)
                Toast.makeText(applicationContext,"$number",Toast.LENGTH_LONG).show()
            }

        }

    }
}