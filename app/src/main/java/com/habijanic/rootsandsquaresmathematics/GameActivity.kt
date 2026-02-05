package com.habijanic.rootsandsquaresmathematics

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
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

private const val TAG = "GameActivityDebug"

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

    private var numberMin = 0
    private var numberMax = 1
    private lateinit var gameType: GameType

    private lateinit var timer : CountDownTimer
    private var currentTimerLimit : Long = 20000
    private var timeLeftInMillis : Long = currentTimerLimit
    private val delayMillis : Long = 500
    private val levelUpDelayMillis : Long = 1500

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

        // Postavi početni minimum ovisno o tipu igre
        numberMin = if (gameType == GameType.ADD || gameType == GameType.SUB) 0 else 1

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
                    
                    // Odmah brišemo upisani broj
                    answerText.setText("")

                    if(userAnswer==correctAnswer){
                        score += 10
                        
                        var isLevelUp = false
                        // Otežaj igru svakih 50 bodova
                        if (score % 50 == 0) {
                            isLevelUp = true
                            val oldMin = numberMin
                            val oldTime = currentTimerLimit
                            
                            // Smanji vrijeme (ne ispod 5s)
                            if (currentTimerLimit > 5000) currentTimerLimit -= 1000
                            
                            // Povećaj minimum, ali zadrži barem 50% raspona dostupnim za raznolikost
                            val minGap = (numberMax * 0.5).toInt().coerceAtLeast(1)
                            if (numberMin < numberMax - minGap) {
                                numberMin++
                                Log.d(TAG, "TEŽINA POVEĆANA! Bodovi: $score | Vrijeme: ${oldTime/1000}s -> ${currentTimerLimit/1000}s | Min: $oldMin -> $numberMin")
                            } else {
                                Log.d(TAG, "VRIJEME SMANJENO! Bodovi: $score | Vrijeme: ${oldTime/1000}s -> ${currentTimerLimit/1000}s | Raspon je ostao isti radi raznolikosti.")
                            }
                        }

                        pauseTimer()
                        resetTimer()
                        
                        val currentDelay = if (isLevelUp) levelUpDelayMillis else delayMillis
                        if (isLevelUp) {
                            correct.text = getString(R.string.level_up)
                            correct.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                            correct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
                            answerText.hint = "" // Makni hint privremeno
                        } else {
                            correct.text = "" 
                        }
                        
                        correct.isVisible=true
                        mainScope.launch{
                            delay(currentDelay)
                            game()
                            scoreText.text = score.toString()
                            
                            // Vraćamo stil na staro za sljedeću rundu
                            correct.isVisible=false
                            correct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                            correct.setTextColor(ContextCompat.getColor(this@GameActivity, android.R.color.white))
                            answerText.hint = getString(R.string.answer) // Vrati hint
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
                            correct.text = ""
                            correct.isVisible=true
                            var color = ContextCompat.getColor(this,R.color.wrong)
                            correct.setBackgroundColor(color)
                            mainScope.launch{
                                delay(delayMillis)
                                game()

                                correct.isVisible=false
                                color = ContextCompat.getColor(this@GameActivity,R.color.correct)
                                correct.setBackgroundColor(color)
                                lifeText.text=life.toString()
                            }
                        }
                    }
                }
            }
        }
    }

    fun game(){
        Log.d(TAG, "Nova runda: Raspon [$numberMin - $numberMax] | Vrijeme: ${currentTimerLimit/1000}s")
        when (gameType) {
            GameType.ADD -> {
                val numberA = Random.nextInt(numberMin, numberMax + 1)
                val numberB = Random.nextInt(numberMin, numberMax + 1)
                questionText.text = getString(R.string.addition_question, getString(R.string.addition_small), numberA, numberB)
                correctAnswer = numberA + numberB
            }
            GameType.SUB -> {
                val numberA = Random.nextInt(numberMin, numberMax + 1)
                var numberB = Random.nextInt(0, numberA + 1)
                
                if (score > 150 && numberA > 1) {
                    while (numberB == 0 || numberB == numberA) {
                        numberB = Random.nextInt(0, numberA + 1)
                    }
                }
                
                questionText.text = getString(R.string.subtraction_question, getString(R.string.subtraction_small), numberA, numberB)
                correctAnswer = numberA - numberB
            }
            GameType.MUL -> {
                var numberA = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                var numberB = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                
                if (score > 150 && numberMax > 5) {
                    while (numberA == 1 || (numberA == 10 && numberMax > 10)) {
                        numberA = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                    }
                    while (numberB == 1 || (numberB == 10 && numberMax > 10)) {
                        numberB = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                    }
                }
                
                questionText.text = getString(R.string.multiplication_question, getString(R.string.multiplication_small), numberA, numberB)
                correctAnswer = numberA * numberB
            }
            GameType.DIV -> {
                var divisor = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                var result = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                
                if (score > 150 && numberMax > 2) {
                    while (divisor == 1) {
                        divisor = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                    }
                    while (result == 1) {
                        result = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                    }
                }
                
                correctAnswer = result
                val dividend = divisor * correctAnswer
                questionText.text = getString(R.string.division_question, getString(R.string.division_small), dividend, divisor)
            }
            GameType.SQUARES -> {
                var number = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                if (score > 150 && numberMax > 1 && number == 1) {
                    number = Random.nextInt(2, numberMax + 1)
                }
                questionText.text = getString(R.string.square_question, getString(R.string.square_small), number)
                correctAnswer = number * number
            }
            GameType.ROOTS -> {
                var result = Random.nextInt(maxOf(1, numberMin), numberMax + 1)
                if (score > 150 && numberMax > 1 && result == 1) {
                    result = Random.nextInt(2, numberMax + 1)
                }
                correctAnswer = result
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

                if (life==0) {
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
        timeLeftInMillis = currentTimerLimit
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
