package com.example.ios_emoji

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import com.example.composeemojilibrary.emoji.CacheKey
import com.example.composeemojilibrary.emoji.Emoji
import java.lang.ref.SoftReference


class IosEmoji : Emoji {
    companion object {
        private const val CACHE_SIZE = 100
        private const val SPRITE_SIZE = 64
        private const val SPRITE_SIZE_INC_BORDER = 66
        private const val NUM_STRIPS = 56
        private val LOCK = Any()
        private val STRIP_REFS: Array<SoftReference<Bitmap>> = emptyArray()
        private val BITMAP_CACHE: LruCache<CacheKey, Bitmap> = LruCache(CACHE_SIZE)

        init {
            for (i in 0 until NUM_STRIPS) {
                STRIP_REFS[i] = SoftReference<Bitmap>(null)
            }
        }
    }

    private val x: Int
    private val y: Int

    constructor(
        codePoints: IntArray, shortcodes: Array<String?>, x: Int, y: Int,
        isDuplicate: Boolean
    ) : super(codePoints, shortcodes, -1, isDuplicate) {
        this.x = x
        this.y = y
    }

    constructor(
        codePoint: Int, shortcodes: Array<String?>, x: Int, y: Int,
        isDuplicate: Boolean
    ) : super(codePoint, shortcodes, -1, isDuplicate) {
        this.x = x
        this.y = y
    }

    constructor(
        codePoint: Int, shortcodes: Array<String?>, x: Int, y: Int,
        isDuplicate: Boolean, vararg variants: Emoji
    ) : super(codePoint, shortcodes, -1, isDuplicate, *variants) {
        this.x = x
        this.y = y
    }

    constructor(
        codePoints: IntArray, shortcodes: Array<String?>, x: Int, y: Int,
        isDuplicate: Boolean, vararg variants: Emoji
    ) : super(codePoints, shortcodes, -1, isDuplicate, *variants) {
        this.x = x
        this.y = y
    }

    override fun getDrawable(context: Context): Drawable {
        val key = CacheKey(x, y)
        val bitmap: Bitmap? = BITMAP_CACHE.get(key)
        if (bitmap != null) {
            return BitmapDrawable(context.resources, bitmap)
        }
        val strip = loadStrip(context)
        val cut = Bitmap.createBitmap(
            strip!!,
            1,
            y * SPRITE_SIZE_INC_BORDER + 1,
            SPRITE_SIZE,
            SPRITE_SIZE
        )
        BITMAP_CACHE.put(key, cut)
        return BitmapDrawable(context.resources, cut)
    }

    private fun loadStrip(context: Context): Bitmap? {
        var strip: Bitmap? = STRIP_REFS[x].get()
        if (strip == null) {
            synchronized(LOCK) {
                strip = STRIP_REFS[x].get()
                if (strip == null) {
                    val resources: Resources = context.resources
                    val resId: Int = resources.getIdentifier(
                        "emoji_ios_sheet_$x",
                        "drawable", context.packageName
                    )
                    strip = BitmapFactory.decodeResource(resources, resId)
                    STRIP_REFS[x] = SoftReference(strip)
                }
            }
        }
        return strip
    }

    override  fun destroy() {
        synchronized(LOCK) {
            BITMAP_CACHE.evictAll()
            for (i in 0 until NUM_STRIPS) {
                val strip = STRIP_REFS[i].get() as Bitmap
                strip.recycle()
                STRIP_REFS[i].clear()
            }
        }
    }
}
