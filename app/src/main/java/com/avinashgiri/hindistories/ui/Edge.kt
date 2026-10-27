package com.avinashgiri.hindistories.ui

import android.app.Activity
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Edge to edge helpers.
 *
 * From Android 15 the window draws behind the status and navigation bars, and
 * in Android 16 there is no way to opt out. Anything we place at the very top
 * or bottom of a screen has to be pushed clear of those bars by hand, or the
 * system swallows the taps.
 */

/**
 * Lets the window draw behind the system bars and sets the icon colour in
 * those bars.
 *
 * @param lightIcons true for dark icons on a light background, false for
 *                   white icons on a dark background.
 */
fun Activity.drawBehindSystemBars(lightIcons: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowInsetsControllerCompat(window, window.decorView)
    controller.isAppearanceLightStatusBars = lightIcons
    controller.isAppearanceLightNavigationBars = lightIcons
}

/**
 * Calls [onInsets] with the space taken by the system bars and by any display
 * cutout, every time that space changes. It changes on rotation, on a
 * foldable opening, and when the user switches between gesture and button
 * navigation, so this must be a listener rather than a one time read.
 */
fun View.onSystemBarInsets(onInsets: (Insets) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
        onInsets(
            windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
        )
        windowInsets
    }
    ViewCompat.requestApplyInsets(this)
}
