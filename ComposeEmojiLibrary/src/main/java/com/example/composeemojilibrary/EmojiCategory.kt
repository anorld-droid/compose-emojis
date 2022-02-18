package com.example.composeemojilibrary

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.composeemojilibrary.emoji.Emoji


/**
 * Interface for defining a category.
 */
interface EmojiCategory {
    /**
     * Returns all of the emojis it can display.
     */
    val emojis: Array<Emoji>

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
