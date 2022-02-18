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

import android.content.Context
import android.content.SharedPreferences
import com.example.composeemojilibrary.EmojiManager.Companion.instance
import com.example.composeemojilibrary.emoji.Emoji
import java.util.*

class RecentEmojiManager(context: Context) : RecentEmoji {
    private var emojiList = EmojiList(0)
    private val sharedPreferences: SharedPreferences
    override val recentEmojis: Collection<Emoji>
        get() {
            if (emojiList.size() == 0) {
                val savedRecentEmojis = sharedPreferences.getString(RECENT_EMOJIS, "")
                if (savedRecentEmojis!!.isNotEmpty()) {
                    val stringTokenizer = StringTokenizer(savedRecentEmojis, EMOJI_DELIMITER)
                    emojiList = EmojiList(stringTokenizer.countTokens())
                    while (stringTokenizer.hasMoreTokens()) {
                        val token = stringTokenizer.nextToken()
                        val parts = token.split(TIME_DELIMITER.toRegex()).toTypedArray()
                        if (parts.size == 2) {
                            val emoji: Emoji? = instance.findEmoji(parts[0])
                            if (emoji != null && emoji.length == parts[0].length) {
                                val timestamp = parts[1].toLong()
                                emojiList.add(emoji, timestamp)
                            }
                        }
                    }
                } else {
                    emojiList = EmojiList(0)
                }
            }
            return emojiList.getEmojis()
        }

    override fun addEmoji(emoji: Emoji) {
        emojiList.add(emoji)
    }

    override fun persist() {
        if (emojiList.size() > 0) {
            val stringBuilder = StringBuilder(emojiList.size() * EMOJI_GUESS_SIZE)
            for (i in 0 until emojiList.size()) {
                val data = emojiList[i]
                stringBuilder.append(data.emoji.unicode)
                    .append(TIME_DELIMITER)
                    .append(data.timestamp)
                    .append(EMOJI_DELIMITER)
            }
            stringBuilder.setLength(stringBuilder.length - EMOJI_DELIMITER.length)
            sharedPreferences.edit().putString(RECENT_EMOJIS, stringBuilder.toString()).apply()
        }
    }

    internal class EmojiList(size: Int) {
        val emojis: MutableList<Data>
        @JvmOverloads
        fun add(emoji: Emoji, timestamp: Long = System.currentTimeMillis()) {
            val iterator = emojis.iterator()
            val emojiBase = emoji.getBase()
            while (iterator.hasNext()) {
                val data = iterator.next()
                if (data.emoji.getBase() == emojiBase
                ) { // Do the comparison by base so that skin tones are only saved once.
                    iterator.remove()
                }
            }
            emojis.add(0, Data(emoji, timestamp))
            if (emojis.size > MAX_RECENTS) {
                emojis.removeAt(MAX_RECENTS)
            }
        }

        fun getEmojis(): Collection<Emoji> {
            Collections.sort(emojis, COMPARATOR)
            val sortedEmojis: MutableCollection<Emoji> = ArrayList(emojis.size)
            for (data in emojis) {
                sortedEmojis.add(data.emoji)
            }
            return sortedEmojis
        }

        fun size(): Int {
            return emojis.size
        }

        operator fun get(index: Int): Data {
            return emojis[index]
        }

        companion object {
            val COMPARATOR = Comparator<Data> { lhs, rhs ->
                java.lang.Long.valueOf(rhs.timestamp).compareTo(lhs.timestamp)
            }
        }

        init {
            emojis = ArrayList(size)
        }
    }

    internal class Data(val emoji: Emoji, val timestamp: Long)
    companion object {
        private const val PREFERENCE_NAME = "emoji-recent-manager"
        private const val TIME_DELIMITER = ";"
        private const val EMOJI_DELIMITER = "~"
        private const val RECENT_EMOJIS = "recent-emojis"
        const val EMOJI_GUESS_SIZE = 5
        const val MAX_RECENTS = 40
    }

    init {
        sharedPreferences =
            context.applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
}