package com.avinashgiri.hindistories.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avinashgiri.hindistories.data.Block
import com.avinashgiri.hindistories.databinding.ItemBlockImageBinding
import com.avinashgiri.hindistories.databinding.ItemBlockParaBinding
import com.avinashgiri.hindistories.databinding.ItemBlockQuoteBinding

/**
 * Feeds the reader. One view type per block type, so adding pictures to a story
 * is a change to stories.json and nothing else.
 */
class BlockAdapter(
    private val blocks: List<Block>,
    private var bodySizeSp: Float
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ParaHolder(val views: ItemBlockParaBinding) : RecyclerView.ViewHolder(views.root)
    class QuoteHolder(val views: ItemBlockQuoteBinding) : RecyclerView.ViewHolder(views.root)
    class ImageHolder(val views: ItemBlockImageBinding) : RecyclerView.ViewHolder(views.root)

    /** Called when the reader picks a new size in the bottom sheet. */
    fun setBodySizeSp(sizeSp: Float) {
        if (sizeSp == bodySizeSp) return
        bodySizeSp = sizeSp
        notifyItemRangeChanged(0, itemCount)
    }

    override fun getItemViewType(position: Int): Int = when (blocks[position].type) {
        Block.QUOTE -> TYPE_QUOTE
        Block.IMAGE -> TYPE_IMAGE
        else -> TYPE_PARA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_QUOTE -> QuoteHolder(ItemBlockQuoteBinding.inflate(inflater, parent, false))
            TYPE_IMAGE -> ImageHolder(ItemBlockImageBinding.inflate(inflater, parent, false))
            else -> ParaHolder(ItemBlockParaBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val block = blocks[position]
        when (holder) {
            is ParaHolder -> {
                holder.views.paragraph.text = block.text.orEmpty()
                holder.views.paragraph.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySizeSp)
            }

            is QuoteHolder -> {
                holder.views.quote.text = block.text.orEmpty()
                holder.views.quote.setTextSize(TypedValue.COMPLEX_UNIT_SP, bodySizeSp)
            }

            is ImageHolder -> {
                val drawableId = Art.id(block.image)
                if (drawableId != 0) {
                    holder.views.scene.setImageResource(drawableId)
                }
                holder.views.scene.contentDescription = block.caption.orEmpty()
                holder.views.caption.text = block.caption.orEmpty()
                holder.views.caption.visibility =
                    if (block.caption.isNullOrBlank()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = blocks.size

    private companion object {
        const val TYPE_PARA = 0
        const val TYPE_QUOTE = 1
        const val TYPE_IMAGE = 2
    }
}
