package com.avinashgiri.hindistories.data

import android.content.Context

/**
 * Everything the app remembers between launches. In version one that is one
 * number: which of the five font sizes the reader picked.
 */
object AppPrefs {

    private const val FILE = "prem_kahaniyan_prefs"
    private const val KEY_FONT_STEP = "font_step"

    const val MIN_STEP = 0
    const val MAX_STEP = 4
    private const val DEFAULT_STEP = 2

    /** Paragraph size in sp for each step. */
    val BODY_SIZES_SP = floatArrayOf(14f, 16f, 18f, 20f, 23f)

    /** What the font sheet shows next to the controls. */
    val STEP_LABELS = arrayOf("बहुत छोटा", "छोटा", "मध्यम", "बड़ा", "बहुत बड़ा")

    fun fontStep(context: Context): Int = prefs(context)
        .getInt(KEY_FONT_STEP, DEFAULT_STEP)
        .coerceIn(MIN_STEP, MAX_STEP)

    fun setFontStep(context: Context, step: Int) {
        prefs(context).edit()
            .putInt(KEY_FONT_STEP, step.coerceIn(MIN_STEP, MAX_STEP))
            .apply()
    }

    fun bodySizeSp(context: Context): Float = BODY_SIZES_SP[fontStep(context)]

    private fun prefs(context: Context) = context.applicationContext
        .getSharedPreferences(FILE, Context.MODE_PRIVATE)
}
