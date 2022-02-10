/*
 * Copyright (C) 2016 - Niklas Baudy, Ruben Gees, Mario Đanić and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.ios_emoji.category

import com.example.ios_emoji.category.CategoryUtils.concatAll
import com.example.ios_emoji.IosEmojiCategory
import com.example.ios_emoji.IosEmoji
import androidx.annotation.DrawableRes
import com.example.ios_emoji.R
import androidx.annotation.StringRes

class FoodAndDrinkCategory : IosEmojiCategory {
    override val emojis: Array<IosEmoji>
        get() = EMOJI
    @get:DrawableRes
    override val icon: Int
        get() = R.drawable.emoji_ios_category_foodanddrink

    @get:StringRes
    override val categoryName: Int
        get() = R.string.emoji_ios_category_foodanddrink

    companion object {
        private val EMOJI = concatAll(FoodAndDrinkCategoryChunk0.get())
    }
}