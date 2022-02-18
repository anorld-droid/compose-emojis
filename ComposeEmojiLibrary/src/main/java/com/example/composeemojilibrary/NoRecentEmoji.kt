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
package com.example.composeemojilibrary

import com.example.composeemojilibrary.emoji.Emoji

/**
 * Use this class to hide recent Emoji.
 */
class NoRecentEmoji private constructor() : RecentEmoji {
    override val recentEmojis: Collection<Emoji>
        get() = emptyList()

    override fun addEmoji(emoji: Emoji) {
        // Do nothing.
    }

    override fun persist() {
        // Do nothing.
    }

    companion object {
        val INSTANCE: RecentEmoji = NoRecentEmoji()
    }
}