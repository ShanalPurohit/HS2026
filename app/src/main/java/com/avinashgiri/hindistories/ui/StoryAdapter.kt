package com.avinashgiri.hindistories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.data.Story
import com.avinashgiri.hindistories.databinding.ItemStoryCardBinding

/** Feeds the two column grid on the first screen. */
class StoryAdapter(
    private val stories: List<Story>,
    private val onStoryClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.CardHolder>() {

    class CardHolder(val views: ItemStoryCardBinding) : RecyclerView.ViewHolder(views.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val views = ItemStoryCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CardHolder(views)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val story = stories[position]
        val views = holder.views
        val context = views.root.context

        views.title.text = story.title
        views.minutes.text = context.getString(R.string.minutes_format, story.readingMinutes)

        val coverId = Art.id(story.coverImage)
        if (coverId != 0) {
            views.cover.setImageResource(coverId)
        } else {
            views.cover.setImageDrawable(null)
        }
        views.cover.contentDescription = story.title

        views.root.setOnClickListener { onStoryClick(story) }
    }

    override fun getItemCount(): Int = stories.size
}
