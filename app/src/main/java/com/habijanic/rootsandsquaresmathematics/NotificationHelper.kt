package com.habijanic.rootsandsquaresmathematics

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.Calendar

class NotificationHelper(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)

    fun scheduleReminder() {
        val isEnabled = prefs.getBoolean("reminder_enabled", true) // Defaultno uključeno
        if (!isEnabled) {
            cancelReminder()
            return
        }

        val hour = prefs.getInt("reminder_hour", 20) // Defaultno 20h
        val minute = prefs.getInt("reminder_minute", 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun updateSettings(enabled: Boolean, hour: Int, minute: Int) {
        prefs.edit().apply {
            putBoolean("reminder_enabled", enabled)
            putInt("reminder_hour", hour)
            putInt("reminder_minute", minute)
            apply()
        }
        scheduleReminder()
    }

    fun isReminderEnabled(): Boolean = prefs.getBoolean("reminder_enabled", true)
    fun getReminderHour(): Int = prefs.getInt("reminder_hour", 20)
    fun getReminderMinute(): Int = prefs.getInt("reminder_minute", 0)
}