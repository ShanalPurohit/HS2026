package com.avinashgiri.hindistories.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.data.AppPrefs
import com.avinashgiri.hindistories.data.Story
import com.avinashgiri.hindistories.data.StoryRepository
import com.avinashgiri.hindistories.databinding.ActivityReaderBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * Screen two: one story.
 *
 * The cover holds the top of the screen at rest and scrolls away as the reader
 * moves into the story, so the whole screen ends up given over to text. The
 * back and font buttons stay pinned throughout, and the title reappears in the
 * toolbar once the cover has gone.
 */
class ReaderActivity : AppCompatActivity(), FontSizeSheet.OnFontStepChanged {

    private lateinit var views: ActivityReaderBinding
    private lateinit var blockAdapter: BlockAdapter
    private lateinit var story: Story

    private var inkColor: Int = 0
    private var paperColor: Int = 0

    /** Last value handed to the system, so a scroll does not reset it every frame. */
    private var lightStatusIcons: Boolean? = null

    /** Last height applied to the header, so insets do not relayout for nothing. */
    private var appliedCoverHeight = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(views.root)

        inkColor = getColor(R.color.ink)
        paperColor = getColor(R.color.paper)

        // The cover is dark at the top, so the status bar icons start white.
        setStatusIcons(dark = false)
        applyInsets()

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        val loaded = storyId?.let { StoryRepository.byId(this, it) }
        if (loaded == null) {
            finish()
            return
        }
        story = loaded

        val coverId = Art.id(story.coverImage)
        if (coverId != 0) {
            views.cover.setImageResource(coverId)
        }
        views.cover.contentDescription = story.title
        views.collapsedTitle.text = story.title

        // The header scrolls with the story; the blocks follow it.
        blockAdapter = BlockAdapter(story.body, AppPrefs.bodySizeSp(this))
        views.blocks.layoutManager = LinearLayoutManager(this)
        views.blocks.adapter = ConcatAdapter(
            ReaderHeaderAdapter(story.title, story.readingMinutes),
            blockAdapter
        )

        views.back.setOnClickListener { finish() }
        views.share.setOnClickListener { Share.story(this, story) }
        views.fontSize.setOnClickListener {
            FontSizeSheet().show(supportFragmentManager, FontSizeSheet.TAG)
        }

        trackCollapse()
    }

    /**
     * Fades the cover out and the controls over to ink as the header collapses.
     *
     * The buttons are white on a dark circle while they sit on the artwork, and
     * ink with no circle once they sit on paper. Without this they would either
     * vanish against a pale cover or leave two grey discs floating on the page.
     */
    private fun trackCollapse() {
        val backBackground = views.back.background?.mutate()
        val shareBackground = views.share.background?.mutate()
        val fontBackground = views.fontSize.background?.mutate()
        views.back.background = backBackground
        views.share.background = shareBackground
        views.fontSize.background = fontBackground

        views.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
                val range = appBar.totalScrollRange
                val ratio = if (range == 0) 0f
                else (abs(verticalOffset).toFloat() / range).coerceIn(0f, 1f)

                views.cover.alpha = 1f - ratio
                views.coverScrim.alpha = 1f - ratio

                // The title only earns its place once the cover has gone.
                views.collapsedTitle.alpha = ((ratio - 0.55f) / 0.45f).coerceIn(0f, 1f)

                val iconColor = ColorUtils.blendARGB(paperColor, inkColor, ratio)
                views.back.setColorFilter(iconColor)
                views.share.setColorFilter(iconColor)
                views.fontSize.setTextColor(iconColor)

                val circleAlpha = ((1f - ratio) * 255f).toInt().coerceIn(0, 255)
                backBackground?.alpha = circleAlpha
                shareBackground?.alpha = circleAlpha
                fontBackground?.alpha = circleAlpha

                // Dark cover at the top wants white status icons; paper wants dark.
                setStatusIcons(dark = ratio > 0.6f)
            }
        )
    }

    /**
     * Switches the status bar icons between light and dark.
     *
     * The offset listener runs on every scroll frame, and asking the window for
     * the same appearance sixty times a second causes visible flicker on some
     * devices, so this only talks to the system when the value actually
     * changes.
     */
    private fun setStatusIcons(dark: Boolean) {
        if (lightStatusIcons == dark) return
        lightStatusIcons = dark
        drawBehindSystemBars(lightIcons = dark)
    }

    /**
     * Keeps the pinned buttons clear of the status bar and the last paragraph
     * clear of the navigation bar, and gives the cover back the height the
     * status bar would otherwise eat.
     */
    private fun applyInsets() {
        val coverArtHeight = resources.getDimensionPixelSize(R.dimen.reader_cover_height)
        val blocksPaddingBottom = views.blocks.paddingBottom

        views.root.onSystemBarInsets { bars ->
            val wanted = coverArtHeight + bars.top
            if (wanted != appliedCoverHeight) {
                appliedCoverHeight = wanted
                views.collapsing.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = wanted
                }
            }
            views.topBar.updatePadding(top = bars.top, left = bars.left, right = bars.right)
            views.blocks.updatePadding(bottom = blocksPaddingBottom + bars.bottom)
        }
    }

    /** The bottom sheet calls this every time the reader taps minus or plus. */
    override fun onFontStepChanged(step: Int) {
        blockAdapter.setBodySizeSp(AppPrefs.BODY_SIZES_SP[step])
    }

    companion object {
        private const val EXTRA_STORY_ID = "story_id"

        fun intentFor(context: Context, storyId: String): Intent =
            Intent(context, ReaderActivity::class.java)
                .putExtra(EXTRA_STORY_ID, storyId)
    }
}
