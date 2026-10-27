package com.avinashgiri.hindistories.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import com.avinashgiri.hindistories.data.StoryRepository
import com.avinashgiri.hindistories.databinding.ActivityMainBinding

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

        val stories = StoryRepository.all(this)

        views.grid.layoutManager = GridLayoutManager(this, COLUMNS)
        views.grid.setHasFixedSize(true)
        views.grid.adapter = StoryAdapter(stories) { story ->
            startActivity(ReaderActivity.intentFor(this, story.id))
        }
    }

    /** Keeps the heading below the status bar and the last row above the nav bar. */
    private fun applyInsets() {
        val titlePaddingTop = views.appTitle.paddingTop
        val gridPaddingBottom = views.grid.paddingBottom

        views.root.onSystemBarInsets { bars ->
            views.appTitle.updatePadding(top = titlePaddingTop + bars.top)
            views.grid.updatePadding(bottom = gridPaddingBottom + bars.bottom)
            views.root.updatePadding(left = bars.left, right = bars.right)
        }
    }

    private companion object {
        const val COLUMNS = 2
    }
}
