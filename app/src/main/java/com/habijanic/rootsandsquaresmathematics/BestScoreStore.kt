package com.habijanic.rootsandsquaresmathematics
import android.content.Context

object BestScoreStore {
    private const val PREFS = "best_score_prefs"
    private const val KEY = "best_score"

    fun get(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY, 0)

    fun updateIfHigher(context: Context, score: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getInt(KEY, 0)
        val best = maxOf(current, score)
        if (best != current) prefs.edit().putInt(KEY, best).apply()
        return best
    }
    fun reset(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(KEY).apply()
    }
}