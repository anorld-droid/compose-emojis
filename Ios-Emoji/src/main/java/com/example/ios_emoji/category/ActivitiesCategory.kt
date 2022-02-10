package com.example.ios_emoji.category

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.ios_emoji.IosEmojiCategory
import com.example.ios_emoji.IosEmoji
import com.example.ios_emoji.R
import com.example.ios_emoji.category.ActivitiesCategoryChunk0.get
import com.example.ios_emoji.category.CategoryUtils.concatAll


class ActivitiesCategory : IosEmojiCategory {
    override val emojis: Array<IosEmoji>
        get() = EMOJI

    @get:DrawableRes
    override val icon: Int
        get() = R.drawable.emoji_ios_category_activities

    @get:StringRes
    override val categoryName: Int
        get() = R.string.emoji_ios_category_activities

    companion object {
        private val EMOJI = concatAll(get())
    }
}