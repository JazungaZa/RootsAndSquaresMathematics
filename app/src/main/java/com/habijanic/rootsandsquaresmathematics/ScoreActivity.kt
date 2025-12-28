package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ScoreActivity : AppCompatActivity() {

    private var score = 0
    private var number = 0
    private var type = 0
    private lateinit var typeText : TextView
    private lateinit var finalScoreText : TextView
    private lateinit var bestScoreText : TextView
    private lateinit var tryAgainButton : Button
    private lateinit var mainMenuButton : Button
    private lateinit var exitButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
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

        typeText = findViewById(R.id.textViewType)
        finalScoreText = findViewById(R.id.textViewFinalScore)
        bestScoreText = findViewById(R.id.BestScore)
        tryAgainButton = findViewById(R.id.buttonTryAgain)
        mainMenuButton = findViewById(R.id.buttonMainMenu)
        exitButton = findViewById(R.id.buttonExit)

        score = intent.getIntExtra("score",0)
        number = intent.getIntExtra("number",0)
        type = intent.getIntExtra("type",0)
        finalScoreText.text = score.toString()
        bestScoreText.text = BestScoreStore.updateIfHigher(this, score, type).toString()

        setTypeText(type)

        tryAgainButton.setOnClickListener {

            val intent = Intent(this@ScoreActivity,GameActivity::class.java)
            intent.putExtra("number",number)
            intent.putExtra("game",type)
            startActivity(intent)
            finish()

        }
        mainMenuButton.setOnClickListener {

            val intent = Intent(this@ScoreActivity,MainActivity::class.java)
            intent.putExtra("numberMax", number)
            startActivity(intent)
            finish()

        }
        exitButton.setOnClickListener {

            //val intent = Intent(Intent.ACTION_MAIN)
            //intent.addCategory(Intent.CATEGORY_HOME)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //startActivity(intent)

            BestScoreStore.reset(this)

            this.finishAffinity()

        }

    }

    private fun setTypeText(type: Int) {
        when (type) {
            0 -> {
                typeText.text=getString(R.string.addition)
            }
            1 -> {
                typeText.text=getString(R.string.subtraction)
            }
            2 -> {
                typeText.text=getString(R.string.multiplication)
            }
            3 -> {
                typeText.text=getString(R.string.division)
            }
            4 -> {
                typeText.text=getString(R.string.squares)
            }
            5 -> {
                typeText.text=getString(R.string.roots)
            }
            else -> {
                typeText.text=" "
            }
        }
    }
}