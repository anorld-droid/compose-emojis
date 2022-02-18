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


class EmojiRange internal constructor( var  start: Int, val end: Int, val emoji: Emoji) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as EmojiRange
        return start == that.start && end == that.end && emoji == that.emoji
    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + end
        result = 31 * result + emoji.hashCode()
        return result
    }
}