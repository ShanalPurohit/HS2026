package com.avinashgiri.hindistories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import com.avinashgiri.hindistories.data.StoryRepository
import com.avinashgiri.hindistories.databinding.ActivityMainBinding
import com.avinashgiri.hindistories.ui.ReaderActivity
import com.avinashgiri.hindistories.ui.SettingsActivity
import com.avinashgiri.hindistories.ui.StoryAdapter
import com.avinashgiri.hindistories.ui.drawBehindSystemBars
import com.avinashgiri.hindistories.ui.onSystemBarInsets

/** Screen one: the grid of story covers. */
class MainActivity : AppCompatActivity() {

    private lateinit var views: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)

        // Paper background, so the status bar icons have to be dark.
        drawBehindSystemBars(lightIcons = true)
        applyInsets()

        views.settings.setOnClickListener {
            startActivity(SettingsActivity.intentFor(this))
        }

        views.grid.layoutManager = GridLayoutManager(this, COLUMNS)
        views.grid.setHasFixedSize(true)

        // Parses off the main thread the first time, then answers from cache.
        StoryRepository.load(this) { stories ->
            views.grid.adapter = StoryAdapter(stories) { story ->
                startActivity(ReaderActivity.intentFor(this, story.id))
            }
        }
    }

    /** Keeps the heading below the status bar and the last row above the nav bar. */
    private fun applyInsets() {
        val headerPaddingTop = views.header.paddingTop
        val gridPaddingBottom = views.grid.paddingBottom

        views.root.onSystemBarInsets { bars ->
            // The header is padded, not the title, so the gear moves down with it.
            views.header.updatePadding(top = headerPaddingTop + bars.top)
            views.grid.updatePadding(bottom = gridPaddingBottom + bars.bottom)
            views.root.updatePadding(left = bars.left, right = bars.right)
        }
    }

    private companion object {
        const val COLUMNS = 2
    }
}