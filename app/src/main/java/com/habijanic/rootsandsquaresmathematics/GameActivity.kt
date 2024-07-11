package com.habijanic.rootsandsquaresmathematics

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var scoreText : TextView
    lateinit var lifeText : TextView
    lateinit var timeText : TextView
    lateinit var questionText : TextView
    lateinit var answerText : EditText
    lateinit var nextButton: Button
    var correctAnswer = 0
    var score = 0
    var life = 3

    var i = 0

    lateinit var timer : CountDownTimer
    private val startTimerInMillis : Long = 60000
    var timeLeftInMillis : Long = startTimerInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        scoreText = findViewById(R.id.textViewScore)
        lifeText = findViewById(R.id.textViewLife)
        timeText = findViewById(R.id.textViewTime)
        questionText = findViewById(R.id.textViewQuestion)
        answerText = findViewById(R.id.editTextNumberAnswer)
        nextButton = findViewById(R.id.buttonNext)

        game()

        nextButton.setOnClickListener {
            if (life==0){

                Toast.makeText(applicationContext,getString(R.string.game_over), Toast.LENGTH_LONG).show()

            }
                else{

                val input = answerText.text.toString()
                if (input == ""){
                    Toast.makeText(applicationContext, getString(R.string.write_an_answer),
                        Toast.LENGTH_SHORT).show()
                }
                else{

                    val userAnswer = input.toInt()

                    if(userAnswer==correctAnswer){
                        score = score + 10
                        pauseTimer()
                        resetTimer()
                        answerText.setText("")
                        game()
                        scoreText.text = score.toString()
                    }
                    else{
                        life--
                        pauseTimer()
                        resetTimer()
                        if(life==0){

                            Toast.makeText(applicationContext,getString(R.string.game_over), Toast.LENGTH_LONG).show()
                        }
                        else{

                            answerText.setText("")
                            lifeText.text=life.toString()
                            game()

                        }

                    }
                }
            }
        }

    }

    fun game(){
        val number = Random.nextInt(0,20)
        questionText.text = getString(R.string.square) + ": $number"
        correctAnswer = number * number
        startTimer()
    }
    fun startTimer(){
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
            }
        }.start()


    }
    fun updateText(){
        val remTime : Int = (timeLeftInMillis / 1000).toInt()
        timeText.text = String.format(Locale.getDefault(),"%02d", remTime) //formatira vrijeme
    }
    fun pauseTimer(){
        timer.cancel()
    }
    fun resetTimer(){
        timeLeftInMillis = startTimerInMillis
        updateText()
    }

}