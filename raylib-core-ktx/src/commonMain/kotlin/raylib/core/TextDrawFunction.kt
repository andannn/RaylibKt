package raylib.core

import kotlinx.cinterop.CValue

interface TextDrawFunction {
    fun drawText(text: String, x: Int, y: Int, fontSize: Int, color: CValue<Color>)
}

fun TextDrawFunction(): TextDrawFunction {
    return DefaultDrawTextFunction()
}

class DefaultDrawTextFunction : TextDrawFunction {
    override fun drawText(text: String, x: Int, y: Int, fontSize: Int, color: CValue<Color>) {
        raylib.interop.DrawText(text, x, y, fontSize, color)
    }
}