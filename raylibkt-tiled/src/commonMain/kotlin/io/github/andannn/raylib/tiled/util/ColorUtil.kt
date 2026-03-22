/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.util

import io.github.andannn.raylib.foundation.Color
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents

internal fun String.toRaylibColor(): CValue<Color> {
    val cleanHex = this.removePrefix("#")
    val rayColor = cValue<Color>().useContents {
        when (cleanHex.length) {
            8 -> {
                val argb = cleanHex.toUInt(16)
                a = ((argb shr 24) and 0xFFu).toUByte()
                r = ((argb shr 16) and 0xFFu).toUByte()
                g = ((argb shr 8) and 0xFFu).toUByte()
                b = (argb and 0xFFu).toUByte()
            }

            6 -> {
                val rgb = cleanHex.toUInt(16)
                a = 255.toUByte()
                r = ((rgb shr 16) and 0xFFu).toUByte()
                g = ((rgb shr 8) and 0xFFu).toUByte()
                b = (rgb and 0xFFu).toUByte()
            }

            else -> {
                r = 255.toUByte()
                g = 255.toUByte()
                b = 255.toUByte()
                a = 255.toUByte()
            }
        }

        readValue()
    }


    return rayColor
}

@PublishedApi
internal fun CValue<Color>.multiplyAlpha(opacity: Float): CValue<Color> {
    return this.useContents {
        val originalAlpha = a.toInt()
        val finalAlpha = (originalAlpha * opacity).toInt().coerceIn(0, 255)

        a = finalAlpha.toUByte()

        readValue()
    }
}