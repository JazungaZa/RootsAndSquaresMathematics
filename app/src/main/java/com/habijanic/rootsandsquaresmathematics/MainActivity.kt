package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    lateinit var exitButton : Button
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
        // Remove background color of navigation bar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.navigationBarColor = resources.getColor(android.R.color.transparent)



        numUpTo = findViewById(R.id.editTextNumber)
        square = findViewById(R.id.buttonSquares)
        root = findViewById(R.id.buttonRoots)
        exitButton = findViewById(R.id.buttonExitMain)

        square.setOnClickListener {

            val input = numUpTo.text.toString()
            if (input.isNullOrEmpty()){
                Toast.makeText(applicationContext,getString(R.string.enter_a_number),Toast.LENGTH_SHORT).show()
            }
            else if (Integer.parseInt(input) < 2){
                Toast.makeText(applicationContext,getString(R.string.number_to_small),Toast.LENGTH_SHORT).show()
            }
            else{
                number = Integer.parseInt(input)
                val intent = Intent(this@MainActivity,GameActivity::class.java)
                intent.putExtra("number",number)
                intent.putExtra("game",0)
                startActivity(intent)
            }

        }
        root.setOnClickListener {

            val input = numUpTo.text.toString()
            if (input.isNullOrEmpty()){
                Toast.makeText(applicationContext,getString(R.string.enter_a_number),Toast.LENGTH_SHORT).show()
            }
            else if (Integer.parseInt(input) < 2){
                Toast.makeText(applicationContext,getString(R.string.number_to_small),Toast.LENGTH_SHORT).show()
            }
            else{
                number = Integer.parseInt(input)
                val intent = Intent(this@MainActivity,GameActivity::class.java)
                intent.putExtra("number",number)
                intent.putExtra("game",1)
                startActivity(intent)
            }

        }
        exitButton.setOnClickListener {

            //val intent = Intent(Intent.ACTION_MAIN)
            //intent.addCategory(Intent.CATEGORY_HOME)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivity(intent)
            this.finishAffinity()

        }

    }
}