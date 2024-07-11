package com.habijanic.rootsandsquaresmathematics

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScoreActivity : AppCompatActivity() {

    var score = 0
    lateinit var finalScoreText : TextView
    lateinit var tryAgainButton : Button
    lateinit var mainMenuButton : Button
    lateinit var exitButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        finalScoreText = findViewById(R.id.textViewFinalScore)
        tryAgainButton = findViewById(R.id.buttonTryAgain)
        mainMenuButton = findViewById(R.id.buttonMainMenu)
        exitButton = findViewById(R.id.buttonExit)

        score = intent.getIntExtra("score",0)
        finalScoreText.text = score.toString()
    }
}