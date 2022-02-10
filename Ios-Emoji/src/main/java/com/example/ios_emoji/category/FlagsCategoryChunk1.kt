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

import com.example.ios_emoji.IosEmoji

internal object FlagsCategoryChunk1 {
    fun get(): Array<IosEmoji> {
        return arrayOf(
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1E6), arrayOf("flag-va"), 4, 46, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1E8), arrayOf("flag-vc"), 4, 47, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1EA), arrayOf("flag-ve"), 4, 48, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1EC), arrayOf("flag-vg"), 4, 49, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1EE), arrayOf("flag-vi"), 4, 50, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1F3), arrayOf("flag-vn"), 4, 51, false),
            IosEmoji(intArrayOf(0x1F1FB, 0x1F1FA), arrayOf("flag-vu"), 4, 52, false),
            IosEmoji(intArrayOf(0x1F1FC, 0x1F1EB), arrayOf("flag-wf"), 4, 53, false),
            IosEmoji(intArrayOf(0x1F1FC, 0x1F1F8), arrayOf("flag-ws"), 4, 54, false),
            IosEmoji(intArrayOf(0x1F1FD, 0x1F1F0), arrayOf("flag-xk"), 4, 55, false),
            IosEmoji(intArrayOf(0x1F1FE, 0x1F1EA), arrayOf("flag-ye"), 4, 56, false),
            IosEmoji(intArrayOf(0x1F1FE, 0x1F1F9), arrayOf("flag-yt"), 5, 0, false),
            IosEmoji(intArrayOf(0x1F1FF, 0x1F1E6), arrayOf("flag-za"), 5, 1, false),
            IosEmoji(intArrayOf(0x1F1FF, 0x1F1F2), arrayOf("flag-zm"), 5, 2, false),
            IosEmoji(intArrayOf(0x1F1FF, 0x1F1FC), arrayOf("flag-zw"), 5, 3, false),
            IosEmoji(
                intArrayOf(0x1F3F4, 0xE0067, 0xE0062, 0xE0065, 0xE006E, 0xE0067, 0xE007F),
                arrayOf("flag-england"),
                11,
                14,
                false
            ),
            IosEmoji(
                intArrayOf(0x1F3F4, 0xE0067, 0xE0062, 0xE0073, 0xE0063, 0xE0074, 0xE007F),
                arrayOf("flag-scotland"),
                11,
                15,
                false
            ),
            IosEmoji(
                intArrayOf(0x1F3F4, 0xE0067, 0xE0062, 0xE0077, 0xE006C, 0xE0073, 0xE007F),
                arrayOf("flag-wales"),
                11,
                16,
                false
            )
        )
    }
}