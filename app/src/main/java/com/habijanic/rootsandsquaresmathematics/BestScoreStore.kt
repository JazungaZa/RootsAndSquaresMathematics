package com.habijanic.rootsandsquaresmathematics
import android.content.Context

object BestScoreStore {
    private const val PREFS = "best_score_prefs_v2"
    private const val KEY_PREFIX = "best_score_"

    private fun key(type: GameType, maxNumber: Int): String =
        "${KEY_PREFIX}${type.name}_$maxNumber"

    fun get(context: Context, type: GameType, maxNumber: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(key(type, maxNumber), 0)
    }

    fun updateIfHigher(context: Context, score: Int, type: GameType, maxNumber: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val k = key(type, maxNumber)
        val current = prefs.getInt(k, 0)
        val best = maxOf(current, score)
        if (best != current) prefs.edit().putInt(k, best).apply()
        return best
    }

    fun reset(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().clear().apply()
    }
}