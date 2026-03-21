/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.CValue
import raylib.interop.DrawFPS
import raylib.interop.DrawTextEx
import raylib.interop.GetFontDefault

interface TextDrawFunction {
    val defaultFont: CValue<Font>

    fun drawFPS(x: Int, y: Int)

    fun drawText(
        text: String,
        position: CValue<Vector2>,
        fontSize: Int,
        color: CValue<Color>,
        spacing: Float = 2f,
        font: CValue<Font> = defaultFont
    )

    fun drawText(
        text: String,
        x: Int,
        y: Int,
        fontSize: Int,
        color: CValue<Color>,
        spacing: Float = 2f,
        font: CValue<Font> = defaultFont
    ) = drawText(
        text,
        Vector2(x.toFloat(), y.toFloat()),
        fontSize,
        color,
        spacing,
        font
    )
}

fun TextDrawFunction(): TextDrawFunction {
    return DefaultDrawTextFunction()
}

class DefaultDrawTextFunction : TextDrawFunction {
    override val defaultFont: CValue<Font>
        get() = GetFontDefault()

    override fun drawFPS(x: Int, y: Int) {
        DrawFPS(x, y)
    }

    override fun drawText(
        text: String,
        position: CValue<Vector2>,
        fontSize: Int,
        color: CValue<Color>,
        spacing: Float,
        font: CValue<Font>
    ) {
        DrawTextEx(
            font = font,
            text = text,
            position = position,
            fontSize = fontSize.toFloat(),
            spacing = spacing,
            tint = color
        )
    }
}