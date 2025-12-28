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
    private lateinit var gameType: GameType

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

        score = intent.getIntExtra(IntentKeys.SCORE, 0)
        number = intent.getIntExtra(IntentKeys.NUMBER_MAX, 0)
        val typeName = intent.getStringExtra(IntentKeys.GAME_TYPE) ?: GameType.ADD.name
        gameType = GameType.valueOf(typeName)


        finalScoreText.text = score.toString()
        bestScoreText.text = BestScoreStore.updateIfHigher(this, score, gameType, number).toString()

        setTypeText(gameType)

        tryAgainButton.setOnClickListener {

            val intent = Intent(this@ScoreActivity,GameActivity::class.java)
            intent.putExtra(IntentKeys.NUMBER_MAX, number)
            intent.putExtra(IntentKeys.GAME_TYPE, gameType.name)
            startActivity(intent)
            finish()

        }
        mainMenuButton.setOnClickListener {

            val intent = Intent(this@ScoreActivity,MainActivity::class.java)
            intent.putExtra(IntentKeys.NUMBER_MAX, number)
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

    private fun setTypeText(type: GameType) {
        typeText.text = when (type) {
            GameType.ADD -> getString(R.string.addition)
            GameType.SUB -> getString(R.string.subtraction)
            GameType.MUL -> getString(R.string.multiplication)
            GameType.DIV -> getString(R.string.division)
            GameType.SQUARES -> getString(R.string.squares)
            GameType.ROOTS -> getString(R.string.roots)
        }
    }
}