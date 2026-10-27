package com.avinashgiri.hindistories.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.data.Story

/**
 * Hands a story to whatever the reader wants to send it with.
 *
 * What goes out is the title, the one line summary and the store link. The
 * story text itself is deliberately left out. Pasting a whole story into
 * WhatsApp gives the reading away and removes any reason to install the app,
 * which is the opposite of what a share link is for. A teaser plus a link is
 * what makes someone open the store.
 */
object Share {

    fun story(activity: Activity, story: Story) {
        val message = activity.getString(
            R.string.share_story_format,
            story.title,
            story.summary,
            activity.getString(R.string.app_name),
            activity.getString(R.string.share_app_url)
        )
        send(activity, subject = story.title, message = message)
    }

    /** The app on its own, with no particular story attached. */
    fun app(activity: Activity) {
        val message = activity.getString(
            R.string.share_app_format,
            activity.getString(R.string.app_name),
            activity.getString(R.string.share_app_url)
        )
        send(activity, subject = activity.getString(R.string.app_name), message = message)
    }

    private fun send(activity: Activity, subject: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            // Shows as the heading on the Android 13 and later share sheet.
            putExtra(Intent.EXTRA_TITLE, subject)
        }
        try {
            activity.startActivity(
                Intent.createChooser(intent, activity.getString(R.string.action_share))
            )
        } catch (notFound: ActivityNotFoundException) {
            // A device with nothing that accepts text is rare but possible.
            Toast.makeText(activity, R.string.share_no_app, Toast.LENGTH_SHORT).show()
        }
    }
}
