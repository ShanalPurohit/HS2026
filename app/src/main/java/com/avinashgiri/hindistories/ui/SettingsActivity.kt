package com.avinashgiri.hindistories.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.databinding.ActivitySettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Privacy policy, share the app, and which version this is. */
class SettingsActivity : AppCompatActivity() {

    private lateinit var views: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(views.root)

        // Paper background throughout, so the status bar icons are dark.
        drawBehindSystemBars(lightIcons = true)
        applyInsets()

        views.versionName.text = versionName()

        views.back.setOnClickListener { finish() }
        views.rowShare.setOnClickListener { Share.app(this) }
        views.rowPrivacy.setOnClickListener { showPrivacyPolicy() }
    }

    /**
     * The policy, in a dialog rather than a screen of its own.
     *
     * This app collects nothing, so the policy is short enough that a dialog
     * reads better than another activity. Material scrolls a long message for
     * us, so no custom layout is needed.
     */
    private fun showPrivacyPolicy() {
        val privacyUrl = "https://giriavinash.blogspot.com/p/privacy-policy_01844799716.html"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
        startActivity(intent)
    }

    /**
     * The version as the user's phone sees it, read from the installed package
     * rather than hardcoded, so it can never drift from what shipped.
     */
    private fun versionName(): String = try {
        @Suppress("DEPRECATION") // the newer overload needs API 33; minSdk here is 24
        packageManager.getPackageInfo(packageName, 0).versionName ?: FALLBACK_VERSION
    } catch (notFound: PackageManager.NameNotFoundException) {
        FALLBACK_VERSION
    }

    /** Keeps the top bar below the status bar and the last row above the nav bar. */
    private fun applyInsets() {
        val scrollPaddingBottom = views.scroll.paddingBottom
        views.root.onSystemBarInsets { bars ->
            views.topBar.updatePadding(top = bars.top, left = bars.left, right = bars.right)
            views.scroll.updatePadding(bottom = scrollPaddingBottom + bars.bottom)
        }
    }

    companion object {
        private const val FALLBACK_VERSION = "1.0"

        fun intentFor(context: Context): Intent =
            Intent(context, SettingsActivity::class.java)
    }
}
