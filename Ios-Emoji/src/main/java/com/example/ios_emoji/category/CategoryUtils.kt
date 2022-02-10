package com.example.ios_emoji.category

import com.example.ios_emoji.IosEmoji
import java.util.*


public object CategoryUtils {
    public fun concatAll(first: Array<IosEmoji>, vararg rest: Array<IosEmoji>): Array<IosEmoji> {
        var totalLength = first.size
        for (array in rest) {
            totalLength += array.size
        }
        val result: Array<IosEmoji> = Arrays.copyOf(first, totalLength)
        var offset = first.size
        for (array in rest) {
            System.arraycopy(array, 0, result, offset, array.size)
            offset += array.size
        }
        return result
    }
}
