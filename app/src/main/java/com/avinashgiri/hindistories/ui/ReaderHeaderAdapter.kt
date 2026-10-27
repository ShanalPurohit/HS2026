package com.avinashgiri.hindistories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinashgiri.hindistories.R
import com.avinashgiri.hindistories.databinding.ItemReaderHeaderBinding

/**
 * One row: the story title and its reading time.
 *
 * Kept as its own adapter and joined to [BlockAdapter] with a ConcatAdapter,
 * rather than added to BlockAdapter as another view type. That way BlockAdapter
 * still maps position to block one for one, with no offset arithmetic to get
 * wrong, and the font size code does not have to skip a header.
 */
class ReaderHeaderAdapter(
    private val title: String,
    private val readingMinutes: Int
) : RecyclerView.Adapter<ReaderHeaderAdapter.HeaderHolder>() {

    class HeaderHolder(val views: ItemReaderHeaderBinding) :
        RecyclerView.ViewHolder(views.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderHolder =
        HeaderHolder(
            ItemReaderHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: HeaderHolder, position: Int) {
        holder.views.title.text = title
        holder.views.minutes.text = holder.views.root.context
            .getString(R.string.minutes_format, readingMinutes)
    }

    override fun getItemCount(): Int = 1
}
