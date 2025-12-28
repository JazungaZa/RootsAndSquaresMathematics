package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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


        add = findViewById(R.id.buttonAddition)
        sub = findViewById(R.id.buttonSubtraction)
        multiply = findViewById(R.id.buttonMultiplication)
        divide = findViewById(R.id.buttonDivision)
        square = findViewById(R.id.buttonSquares)
        root = findViewById(R.id.buttonRoots)

        exitButton = findViewById(R.id.buttonExitMain)

        add.setOnClickListener { showDifficultySheet(GameType.ADD) }
        sub.setOnClickListener { showDifficultySheet(GameType.SUB) }
        multiply.setOnClickListener { showDifficultySheet(GameType.MUL) }
        divide.setOnClickListener { showDifficultySheet(GameType.DIV) }
        square.setOnClickListener { showDifficultySheet(GameType.SQUARES) }
        root.setOnClickListener { showDifficultySheet(GameType.ROOTS) }

        exitButton.setOnClickListener {

            //val intent = Intent(Intent.ACTION_MAIN)
            //intent.addCategory(Intent.CATEGORY_HOME)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivity(intent)
            this.finishAffinity()

        }

    }


    private fun startGame(type: GameType, maxNumber: Int){
        if (maxNumber < 2){
            Toast.makeText(applicationContext,getString(R.string.number_to_small),Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this@MainActivity,GameActivity::class.java)
        intent.putExtra(IntentKeys.NUMBER_MAX,maxNumber)
        intent.putExtra(IntentKeys.GAME_TYPE, type.name)
        startActivity(intent)

    }


    data class Presets(val easy: Int, val medium: Int, val hard: Int)
    private fun presetsFor(type: GameType): Presets{
        return when (type){
            GameType.ADD -> Presets(10, 20, 100)
            GameType.SUB -> Presets(10, 20, 100)
            GameType.MUL -> Presets(5, 10, 12)
            GameType.DIV -> Presets(5, 10, 12)
            GameType.SQUARES -> Presets(10, 15, 20)
            GameType.ROOTS -> Presets(10, 15, 20)
        }
    }

    private fun showDifficultySheet(type: GameType){
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_difficulty, null)
        dialog.setContentView(view)

        val titleDiff = view.findViewById<TextView>(R.id.titleDiff)
        val btnEasy = view.findViewById<Button>(R.id.btnEasy)
        val btnMedium = view.findViewById<Button>(R.id.btnMedium)
        val btnHard = view.findViewById<Button>(R.id.btnHard)
        val editTextMax = view.findViewById<EditText>(R.id.editTextMax)
        val btnPlayCustom = view.findViewById<Button>(R.id.btnPlayCustom)

        val presets = presetsFor(type)

        titleDiff.text = getString(R.string.difficulty)

        btnEasy.text = getString(R.string.difficulty_easy, presets.easy)
        btnMedium.text = getString(R.string.difficulty_medium, presets.medium)
        btnHard.text = getString(R.string.difficulty_hard, presets.hard)


        fun startAndClose(maxNumber: Int){
            dialog.dismiss()
            startGame(type, maxNumber)
        }

        btnEasy.setOnClickListener { startAndClose(presets.easy) }
        btnMedium.setOnClickListener { startAndClose(presets.medium) }
        btnHard.setOnClickListener { startAndClose(presets.hard) }
        btnPlayCustom.setOnClickListener{

            val input = editTextMax.text.toString().trim()
            val value = input.toIntOrNull()

            when {
                input.isEmpty() || value == null -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.enter_a_number),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                value < 2 -> {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.number_to_small),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> startAndClose(value)
            }
        }

        dialog.show()


    }

}