package com.habijanic.rootsandsquaresmathematics

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private lateinit var add : Button
    private lateinit var sub : Button
    private lateinit var multiply : Button
    private lateinit var divide : Button
    private lateinit var square : Button
    private lateinit var root : Button
    private lateinit var instagramButton : ImageButton
    private lateinit var reminderButton : ImageButton

    private lateinit var exitButton : Button
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            notificationHelper.scheduleReminder()
        } else {
            Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
        }
    }

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

        notificationHelper = NotificationHelper(this)
        checkNotificationPermission()

        add = findViewById(R.id.buttonAddition)
        sub = findViewById(R.id.buttonSubtraction)
        multiply = findViewById(R.id.buttonMultiplication)
        divide = findViewById(R.id.buttonDivision)
        square = findViewById(R.id.buttonSquares)
        root = findViewById(R.id.buttonRoots)
        instagramButton = findViewById(R.id.imageButtonInstagram)
        reminderButton = findViewById(R.id.buttonReminder)

        exitButton = findViewById(R.id.buttonExitMain)

        add.setOnClickListener { showDifficultySheet(GameType.ADD) }
        sub.setOnClickListener { showDifficultySheet(GameType.SUB) }
        multiply.setOnClickListener { showDifficultySheet(GameType.MUL) }
        divide.setOnClickListener { showDifficultySheet(GameType.DIV) }
        square.setOnClickListener { showDifficultySheet(GameType.SQUARES) }
        root.setOnClickListener { showDifficultySheet(GameType.ROOTS) }

        instagramButton.setOnClickListener {
            openInstagram("apps_by_amy")
        }

        reminderButton.setOnClickListener {
            showReminderSettings()
        }
        exitButton.setOnClickListener {
            this.finishAffinity()
        }

    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                notificationHelper.scheduleReminder()
            }
        } else {
            notificationHelper.scheduleReminder()
        }
    }

    private fun openInstagram(username: String) {
        val uri = Uri.parse("http://instagram.com/_u/$username")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Ako Instagram nije instaliran, otvori u pregledniku
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$username")))
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
        val dialog = BottomSheetDialog(this)
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

    private fun showReminderSettings() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_reminder, null)
        dialog.setContentView(view)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBoxReminder)
        val btnSetTime = view.findViewById<Button>(R.id.btnSetTime)
        val btnSave = view.findViewById<Button>(R.id.btnSaveReminder)

        var selectedHour = notificationHelper.getReminderHour()
        var selectedMinute = notificationHelper.getReminderMinute()

        checkBox.isChecked = notificationHelper.isReminderEnabled()
        btnSetTime.text = getString(R.string.reminder_set_to, selectedHour, selectedMinute)

        btnSetTime.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                btnSetTime.text = getString(R.string.reminder_set_to, selectedHour, selectedMinute)
            }, selectedHour, selectedMinute, true).show()
        }

        btnSave.setOnClickListener {
            if (checkBox.isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    // Ne zatvaramo dijalog jer čekamo dozvolu
                    return@setOnClickListener
                }
            }

            notificationHelper.updateSettings(checkBox.isChecked, selectedHour, selectedMinute)
            val msg = if (checkBox.isChecked) 
                getString(R.string.reminder_set_to, selectedHour, selectedMinute) 
                else getString(R.string.reminder_off)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

}