package com.habijanic.rootsandsquaresmathematics
import android.content.Context

object BestScoreStore {
    private const val PREFS = "best_score_prefs"
    private const val KEY_SQUARES = "best_score_squares"
    private const val KEY_ROOTS = "best_score_roots"

    fun get(context: Context, type: Int): Int {
        if (type==1){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_ROOTS, 0)
        }
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_SQUARES, 0)
    }
    fun updateIfHigher(context: Context, score: Int, type: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        var current: Int
        var best: Int
        if (type==1){
            current = prefs.getInt(KEY_ROOTS, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_ROOTS, best).apply()
        }else {
            current = prefs.getInt(KEY_SQUARES, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_SQUARES, best).apply()
        }
        return best
    }
    fun reset(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(KEY_SQUARES).remove(KEY_ROOTS).apply()
    }
}