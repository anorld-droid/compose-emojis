package com.example.composeemojilibrary

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


/**
 * Interface for defining a category.
 */
interface EmojiCategory {
    /**
     * Returns all of the emojis it can display.
     */
    val emojis: List<Emoji?>

    /**
     * Returns the icon of the category that should be displayed.
     */
    @get:DrawableRes
    val icon: Int

    /**
     * Returns category name.
     */
    @get:StringRes
    val categoryName: Int
}
