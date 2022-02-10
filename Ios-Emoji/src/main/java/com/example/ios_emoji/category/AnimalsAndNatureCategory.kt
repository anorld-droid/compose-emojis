package com.example.ios_emoji.category

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.ios_emoji.IosEmojiCategory
import com.example.ios_emoji.IosEmoji
import com.example.ios_emoji.R


class AnimalsAndNatureCategory : IosEmojiCategory {
    override val emojis: Array<IosEmoji>
        get() = EMOJI

    @get:DrawableRes
    override val icon: Int
        get() = R.drawable.emoji_ios_category_animalsandnature

    @get:StringRes
    override val categoryName: Int
        get() = R.string.emoji_ios_category_animalsandnature

    companion object {
        val EMOJI = CategoryUtils.concatAll(AnimalsAndNatureCategoryChunk0.get())
    }
}
