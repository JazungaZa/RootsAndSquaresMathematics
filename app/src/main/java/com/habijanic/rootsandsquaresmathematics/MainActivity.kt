package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var numUpTo : EditText
    private lateinit var add : Button
    private lateinit var sub : Button
    private lateinit var multiply : Button
    private lateinit var divide : Button
    private lateinit var square : Button
    private lateinit var root : Button

    private lateinit var exitButton : Button
    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Remove background color of navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)



        numUpTo = findViewById(R.id.editTextNumber)
        add = findViewById(R.id.buttonAddition)
        sub = findViewById(R.id.buttonSubtraction)
        multiply = findViewById(R.id.buttonMultiplication)
        divide = findViewById(R.id.buttonDivision)
        square = findViewById(R.id.buttonSquares)
        root = findViewById(R.id.buttonRoots)

        exitButton = findViewById(R.id.buttonExitMain)

        add.setOnClickListener{
            numberValidation(0)
        }
        sub.setOnClickListener {
            numberValidation(1)
        }
        multiply.setOnClickListener {
            numberValidation(2)
        }
        divide.setOnClickListener {
            numberValidation(3)
        }
        square.setOnClickListener {
            numberValidation(4)
        }
        root.setOnClickListener {
            numberValidation(5)
        }
        exitButton.setOnClickListener {

            //val intent = Intent(Intent.ACTION_MAIN)
            //intent.addCategory(Intent.CATEGORY_HOME)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivity(intent)
            this.finishAffinity()

        }

    }

    private fun numberValidation(type: Int) {
        val input = numUpTo.text.toString()
        if (input.isEmpty()){
            Toast.makeText(applicationContext,getString(R.string.enter_a_number),Toast.LENGTH_SHORT).show()
        }
        else if (Integer.parseInt(input) < 2){
            Toast.makeText(applicationContext,getString(R.string.number_to_small),Toast.LENGTH_SHORT).show()
        }else{
            number = Integer.parseInt(input)
            val intent = Intent(this@MainActivity,GameActivity::class.java)
            intent.putExtra("number",number)
            intent.putExtra("game",type)
            startActivity(intent)
        }
    }
}