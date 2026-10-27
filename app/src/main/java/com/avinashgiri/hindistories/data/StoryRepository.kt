package com.avinashgiri.hindistories.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import org.json.JSONObject

/**
 * The only place that knows where stories come from.
 *
 * The file is read once and kept in memory. The app has no network and no
 * database, so there is nothing else to coordinate.
 *
 * stories.json is about 175 KB, which takes long enough to read and parse
 * that doing it on the main thread shows a blank screen at launch. So the
 * grid screen asks for the stories with [load], which parses on a background
 * thread the first time and answers from the cache every time after. Once the
 * cache is warm, [all] is free, which is why the reader can stay synchronous.
 */
object StoryRepository {

    private const val FILE_NAME = "stories.json"

    @Volatile
    private var cache: List<Story>? = null

    /** True once the stories are in memory, so callers know [all] will not block. */
    val isLoaded: Boolean get() = cache != null

    /**
     * Hands the stories to [onReady] on the main thread.
     *
     * Answers immediately when the cache is warm, so a rotation does not
     * flash an empty grid.
     */
    fun load(context: Context, onReady: (List<Story>) -> Unit) {
        cache?.let {
            onReady(it)
            return
        }
        val app = context.applicationContext
        val main = Handler(Looper.getMainLooper())
        Thread({
            val stories = all(app)
            main.post { onReady(stories) }
        }, "story-load").start()
    }

    /** Blocks on the first call. Use [load] from a screen; this is for once it is warm. */
    fun all(context: Context): List<Story> {
        cache?.let { return it }
        synchronized(this) {
            cache?.let { return it }
            val text = context.applicationContext.assets
                .open(FILE_NAME)
                .bufferedReader()
                .use { it.readText() }
            val parsed = parse(text)
            cache = parsed
            return parsed
        }
    }

    fun byId(context: Context, id: String): Story? =
        all(context).firstOrNull { it.id == id }

    private fun parse(text: String): List<Story> {
        val array = JSONArray(text)
        val stories = ArrayList<Story>(array.length())
        for (i in 0 until array.length()) {
            stories.add(toStory(array.getJSONObject(i)))
        }
        return stories
    }

    private fun toStory(json: JSONObject): Story = Story(
        id = json.getString("id"),
        title = json.getString("title"),
        summary = json.optString("summary"),
        readingMinutes = json.optInt("readingMinutes"),
        coverImage = json.optStringOrNull("coverImage"),
        body = toBlocks(json.optJSONArray("body"))
    )

    private fun toBlocks(array: JSONArray?): List<Block> {
        if (array == null) return emptyList()
        val blocks = ArrayList<Block>(array.length())
        for (i in 0 until array.length()) {
            val json = array.getJSONObject(i)
            blocks.add(
                Block(
                    type = json.optString("type", Block.PARA),
                    text = json.optStringOrNull("text"),
                    image = json.optStringOrNull("image"),
                    caption = json.optStringOrNull("caption")
                )
            )
        }
        return blocks
    }

    /** optString returns the string "null" for a JSON null, which is never what we want. */
    private fun JSONObject.optStringOrNull(key: String): String? =
        if (isNull(key)) null else optString(key).ifBlank { null }
}
