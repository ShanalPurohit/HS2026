package com.avinashgiri.hindistories.ui

import com.avinashgiri.hindistories.R

/**
 * Turns an image name from stories.json into a drawable id.
 *
 * This is a plain map on purpose. Resources.getIdentifier() would also work,
 * but it looks up by name at runtime, so the resource shrinker cannot see the
 * drawables are used and strips them out of the release build.
 *
 * When you add a new cover or scene, add one line here.
 */
object Art {

    private val byName = mapOf(
        "cover_01" to R.drawable.cover_01,
        "cover_02" to R.drawable.cover_02,
        "cover_03" to R.drawable.cover_03,
        "cover_04" to R.drawable.cover_04,
        "cover_05" to R.drawable.cover_05,
        "cover_06" to R.drawable.cover_06,
        "cover_07" to R.drawable.cover_07,
        "cover_08" to R.drawable.cover_08,
        "cover_09" to R.drawable.cover_09,
        "cover_10" to R.drawable.cover_10,
        "cover_11" to R.drawable.cover_11,
        "cover_12" to R.drawable.cover_12,
        "cover_13" to R.drawable.cover_13,
        "cover_14" to R.drawable.cover_14,
        "cover_15" to R.drawable.cover_15,
        "cover_16" to R.drawable.cover_16,
        "cover_17" to R.drawable.cover_17,
        "cover_18" to R.drawable.cover_18,
        "cover_19" to R.drawable.cover_19,
        "cover_20" to R.drawable.cover_20,
        "scene_01" to R.drawable.scene_01,
        "scene_02" to R.drawable.scene_02,
        "scene_03" to R.drawable.scene_03,
        "scene_04" to R.drawable.scene_04,
        "scene_05" to R.drawable.scene_05,
        "scene_06" to R.drawable.scene_06,
        "scene_07" to R.drawable.scene_07,
        "scene_08" to R.drawable.scene_08,
        "scene_09" to R.drawable.scene_09,
        "scene_10" to R.drawable.scene_10,
        "scene_11" to R.drawable.scene_11,
        "scene_12" to R.drawable.scene_12,
        "scene_13" to R.drawable.scene_13,
        "scene_14" to R.drawable.scene_14,
        "scene_15" to R.drawable.scene_15,
        "scene_16" to R.drawable.scene_16,
        "scene_17" to R.drawable.scene_17,
        "scene_18" to R.drawable.scene_18,
        "scene_19" to R.drawable.scene_19,
        "scene_20" to R.drawable.scene_20
    )

    /** Returns 0 when the name is missing or unknown, which callers treat as "no picture". */
    fun id(name: String?): Int = if (name == null) 0 else byName[name] ?: 0
}
