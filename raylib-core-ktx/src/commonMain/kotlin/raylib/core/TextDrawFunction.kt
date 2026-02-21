package raylib.core

import kotlinx.cinterop.CValue

interface TextDrawFunction {
    val defaultFont: CValue<Font>
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
        get() = raylib.interop.GetFontDefault()

    override fun drawText(
        text: String,
        position: CValue<Vector2>,
        fontSize: Int,
        color: CValue<Color>,
        spacing: Float,
        font: CValue<Font>
    ) {
        raylib.interop.DrawTextEx(
            font = font,
            text = text,
            position = position,
            fontSize = fontSize.toFloat(),
            spacing = spacing,
            tint = color
        )
    }
}