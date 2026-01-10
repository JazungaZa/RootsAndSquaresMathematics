package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var scoreText : TextView
    lateinit var lifeText : TextView
    private lateinit var timeText : TextView
    private lateinit var questionText : TextView
    private lateinit var answerText : EditText
    private lateinit var nextButton: Button
    private lateinit var correct : TextView

    private var correctAnswer = 0
    var score = 0
    var life = 3

    var numberMax = 1
    private lateinit var gameType: GameType

    private lateinit var timer : CountDownTimer
    private val startTimerInMillis : Long = 20000
    var timeLeftInMillis : Long = startTimerInMillis
    val delayMillis : Long = 500

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        scoreText = findViewById(R.id.textViewScore)
        lifeText = findViewById(R.id.textViewLife)
        timeText = findViewById(R.id.textViewTime)
        questionText = findViewById(R.id.textViewQuestion)
        answerText = findViewById(R.id.editTextNumberAnswer)
        nextButton = findViewById(R.id.buttonNext)
        correct = findViewById(R.id.textViewCorrect)
        correct.isVisible=false

        numberMax = intent.getIntExtra(IntentKeys.NUMBER_MAX, 1)
        val typeName = intent.getStringExtra(IntentKeys.GAME_TYPE) ?: GameType.ADD.name
        gameType = GameType.valueOf(typeName)




        game()

        nextButton.setOnClickListener {
            if (life==0){

                mainScope.launch {
                    delay(delayMillis)
                    goToScore()
                }
            }else{
                val input = answerText.text.toString()
                if (input == ""){
                    Toast.makeText(applicationContext, getString(R.string.write_an_answer),
                        Toast.LENGTH_SHORT).show()
                }else{
                    val userAnswer = input.toInt()
                    if(userAnswer==correctAnswer){
                        score += 10
                        pauseTimer()
                        resetTimer()
                        correct.isVisible=true
                        mainScope.launch{
                            delay(delayMillis)
                            game()
                            scoreText.text = score.toString()
                            answerText.setText("")
                            correct.isVisible=false
                        }
                    }else{
                        life--
                        pauseTimer()
                        resetTimer()
                        if(life==0){
                            mainScope.launch {
                                delay(delayMillis)

                                goToScore()
                            }
                        }else{
                            correct.isVisible=true
                            var color = ContextCompat.getColor(this,R.color.wrong)
                            correct.setBackgroundColor(color)
                            mainScope.launch{
                                delay(delayMillis)
                                game()

                                correct.isVisible=false
                                color = ContextCompat.getColor(this@GameActivity,R.color.correct)
                                correct.setBackgroundColor(color)
                                answerText.setText("")
                                lifeText.text=life.toString()
                            }
                        }
                    }
                }
            }
        }

    }

    fun game(){

        when (gameType) {
            GameType.ADD -> {
                val numberA = Random.nextInt(1,numberMax+1)
                val numberB = Random.nextInt(0,numberMax+1)
                questionText.text = getString(R.string.addition_question, getString(R.string.addition_small), numberA, numberB)

                correctAnswer = numberA + numberB
            }
            GameType.SUB -> {
                val num1 = Random.nextInt(1, numberMax + 1)
                val num2 = Random.nextInt(0, numberMax + 1)
                val numberA = maxOf(num1, num2)
                val numberB = minOf(num1, num2)
                questionText.text = getString(R.string.subtraction_question, getString(R.string.subtraction_small), numberA, numberB)
                correctAnswer = numberA - numberB
            }
            GameType.MUL -> {
                val numberA = Random.nextInt(1,numberMax+1)
                val numberB = Random.nextInt(1,numberMax+1)
                questionText.text = getString(R.string.multiplication_question, getString(R.string.multiplication_small), numberA, numberB)
                correctAnswer = numberA * numberB
            }
            GameType.DIV -> {
                val divisor = Random.nextInt(1, numberMax + 1)
                correctAnswer = Random.nextInt(1, numberMax + 1)
                val dividend = divisor * correctAnswer
                questionText.text = getString(R.string.division_question, getString(R.string.division_small), dividend, divisor)
            }
            GameType.SQUARES -> {
                val number = Random.nextInt(1,numberMax+1)
                questionText.text = getString(R.string.square_question, getString(R.string.square_small), number)
                correctAnswer = number * number
            }
            GameType.ROOTS -> {
                correctAnswer = Random.nextInt(1, numberMax + 1)
                val square = correctAnswer * correctAnswer
                questionText.text = getString(R.string.root_question, getString(R.string.root_small), square)
            }
        }
        startTimer()
    }
    private fun startTimer(){
        timer = object : CountDownTimer(timeLeftInMillis, 1000){
            override fun onTick(millisUntilFinished : Long){
                timeLeftInMillis = millisUntilFinished
                updateText()
            }
            override fun onFinish() {
                pauseTimer()
                resetTimer()
                updateText()

                life--
                lifeText.text = life.toString()

                if (life==0)
                {
                    mainScope.launch {
                        delay(delayMillis)

                        goToScore()
                    }
                }else{
                    game()
                }
            }
        }.start()
    }
    fun updateText(){
        val remTime : Int = (timeLeftInMillis / 1000).toInt()
        timeText.text = String.format(Locale.getDefault(),"%02d", remTime)
    }
    fun pauseTimer(){
        timer.cancel()
    }
    fun resetTimer(){
        timeLeftInMillis = startTimerInMillis
        updateText()
    }
    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
    private fun goToScore() {
        val intent = Intent(this@GameActivity, ScoreActivity::class.java)
        intent.putExtra(IntentKeys.SCORE, score)
        intent.putExtra(IntentKeys.NUMBER_MAX, numberMax)
        intent.putExtra(IntentKeys.GAME_TYPE, gameType.name)
        startActivity(intent)
        finish()
    }

}
