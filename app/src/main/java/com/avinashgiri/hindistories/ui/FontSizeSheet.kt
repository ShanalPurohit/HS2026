package com.avinashgiri.hindistories.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.data.AppPrefs
import com.avinashgiri.hindistories.databinding.SheetFontSizeBinding

/** The bottom sheet with minus, five dots and plus. */
class FontSizeSheet : BottomSheetDialogFragment() {

    /** The host activity implements this to repaint the story as the size changes. */
    interface OnFontStepChanged {
        fun onFontStepChanged(step: Int)
    }

    private var binding: SheetFontSizeBinding? = null
    private lateinit var dots: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val views = SheetFontSizeBinding.inflate(inflater, container, false)
        binding = views
        return views.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val views = binding ?: return

        dots = listOf(views.dot0, views.dot1, views.dot2, views.dot3, views.dot4)

        views.smaller.setOnClickListener { change(-1) }
        views.bigger.setOnClickListener { change(+1) }
        views.done.setOnClickListener { dismiss() }

        render()
    }

    private fun change(delta: Int) {
        val context = context ?: return
        val next = (AppPrefs.fontStep(context) + delta)
            .coerceIn(AppPrefs.MIN_STEP, AppPrefs.MAX_STEP)
        AppPrefs.setFontStep(context, next)
        (activity as? OnFontStepChanged)?.onFontStepChanged(next)
        render()
    }

    private fun render() {
        val context = context ?: return
        val views = binding ?: return

        val step = AppPrefs.fontStep(context)
        val sizeSp = AppPrefs.BODY_SIZES_SP[step]

        views.stepLabel.text = AppPrefs.STEP_LABELS[step]
        views.preview.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)

        dots.forEachIndexed { index, dot ->
            dot.setImageResource(
                if (index == step) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }

        views.smaller.isEnabled = step > AppPrefs.MIN_STEP
        views.bigger.isEnabled = step < AppPrefs.MAX_STEP
        views.smaller.alpha = if (views.smaller.isEnabled) 1f else 0.4f
        views.bigger.alpha = if (views.bigger.isEnabled) 1f else 0.4f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val TAG = "font_size_sheet"
    }
}
