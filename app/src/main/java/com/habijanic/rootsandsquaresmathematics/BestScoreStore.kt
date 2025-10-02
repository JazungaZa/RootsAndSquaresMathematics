package com.habijanic.rootsandsquaresmathematics
import android.content.Context

object BestScoreStore {
    private const val PREFS = "best_score_prefs"
    private const val KEY_ADD = "best_score_add"
    private const val KEY_SUB = "best_score_sub"
    private const val KEY_MULTIPLY = "best_score_multiply"
    private const val KEY_DIVIDE = "best_score_divide"
    private const val KEY_SQUARES = "best_score_squares"
    private const val KEY_ROOTS = "best_score_roots"

    fun get(context: Context, type: Int): Int {
        if (type==0){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_ADD, 0)
        } else if (type==1){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_SUB, 0)
        }else if (type==2){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_MULTIPLY, 0)
        }else if (type==3){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_DIVIDE, 0)
        }else if (type==4){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_SQUARES, 0)
        }else if (type==5){
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_ROOTS, 0)
        }
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_ADD, 0)
    }
    fun updateIfHigher(context: Context, score: Int, type: Int): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        var current: Int
        var best: Int
        if (type==0){
            current = prefs.getInt(KEY_ADD, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_ADD, best).apply()
        }else if (type==1) {
            current = prefs.getInt(KEY_SUB, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_SUB, best).apply()
        }else if (type==2) {
            current = prefs.getInt(KEY_MULTIPLY, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_MULTIPLY, best).apply()
        }else if (type==3) {
            current = prefs.getInt(KEY_DIVIDE, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_DIVIDE, best).apply()
        }else if (type==4) {
            current = prefs.getInt(KEY_SQUARES, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_SQUARES, best).apply()
        }else if (type==5) {
            current = prefs.getInt(KEY_ROOTS, 0)
            best = maxOf(current, score)
            if (best != current) prefs.edit().putInt(KEY_ROOTS, best).apply()
        }else{
            best = 0
        }
        return best
    }
    fun reset(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(KEY_ADD).remove(KEY_SUB).remove(KEY_MULTIPLY).remove(KEY_DIVIDE).remove(KEY_SQUARES).remove(KEY_ROOTS).apply()
    }
}