package com.example.composeemojilibrary.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import java.io.Serializable
import java.util.*


open class Emoji(
    codePoints: IntArray, shortcodes: Array<String?>,
    @DrawableRes val resource: Int, isDuplicate: Boolean,
    vararg variants: Emoji
) : Serializable {
    val unicode: String
    private val shortcodes: Array<String?>


    val isDuplicate: Boolean
    private val variants: List<Emoji>
    private var base: Emoji? = null

    constructor(
        codePoints: IntArray, shortcodes: Array<String?>,
        @DrawableRes resource: Int, isDuplicate: Boolean
    ) : this(codePoints, shortcodes, resource, isDuplicate, *emptyArray<Emoji>())

    constructor(
        codePoint: Int, shortcodes: Array<String?>,
        @DrawableRes resource: Int, isDuplicate: Boolean
    ) : this(codePoint, shortcodes, resource, isDuplicate, *emptyArray<Emoji>())

    constructor(
        codePoint: Int, shortcodes: Array<String?>,
        @DrawableRes resource: Int, isDuplicate: Boolean,
        vararg variants: Emoji
    ) : this(intArrayOf(codePoint), shortcodes, resource, isDuplicate, *variants)

    fun getShortcodes(): List<String?> {
        return listOf(*shortcodes)
    }

    open fun getDrawable(context: Context): Drawable {
        return AppCompatResources.getDrawable(context, resource)!!
    }

    fun getVariants(): List<Emoji> {
        return ArrayList(variants)
    }

    fun getBase(): Emoji {
        var result = this
        while (result.base != null) {
            result = result.base!!
        }
        return result
    }

    val length: Int
        get() = unicode.length

    fun hasVariants(): Boolean {
        return variants.isNotEmpty()
    }

    open fun destroy() {
        // For inheritors to override.
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val emoji = o as Emoji
        return (resource == emoji.resource && unicode == emoji.unicode && Arrays.equals(
            shortcodes,
            emoji.shortcodes
        ) && variants == emoji.variants)
    }

    override fun hashCode(): Int {
        var result = unicode.hashCode()
        result = 31 * result + Arrays.hashCode(shortcodes)
        result = 31 * result + resource
        result = 31 * result + variants.hashCode()
        return result
    }

    companion object {
        private const val serialVersionUID = 3L
        private val EMPTY_EMOJI_LIST: List<Emoji> = emptyList()
    }

    init {
        unicode = String(codePoints, 0, codePoints.size)
        this.shortcodes = shortcodes
        this.isDuplicate = isDuplicate
        this.variants = if (variants.isEmpty()) EMPTY_EMOJI_LIST else variants.asList()
        for (variant in variants) {
            variant.base = this
        }
    }
}
