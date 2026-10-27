package com.avinashgiri.hindistories.data

/**
 * One story, exactly as it appears in assets/stories.json.
 *
 * The body is a list of blocks rather than one long string. That is what lets a
 * story mix text and pictures without any change to the reader code.
 */
data class Story(
    val id: String,
    val title: String,
    val summary: String,
    val readingMinutes: Int,
    val coverImage: String?,
    val body: List<Block>
)

/**
 * One piece of a story body.
 *
 * type "para"  -> text is a paragraph
 * type "quote" -> text is a closing line, shown in the accent colour
 * type "image" -> image is a drawable name, caption is the line under it
 */
data class Block(
    val type: String,
    val text: String?,
    val image: String?,
    val caption: String?
) {
    companion object {
        const val PARA = "para"
        const val QUOTE = "quote"
        const val IMAGE = "image"
    }
}
