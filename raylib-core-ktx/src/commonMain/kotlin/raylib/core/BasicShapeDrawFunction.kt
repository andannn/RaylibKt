package raylib.core

import kotlinx.cinterop.CValue

interface BasicShapeDrawFunction {
    fun drawCircle(centerX: Int, centerY: Int, radius: Float, color: CValue<Color>) =
        drawCircle(Vector2(centerX.toFloat(), centerY.toFloat()), radius, color)

    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>)
    fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)
    fun drawRectangle(
        position: CValue<raylib.interop.Vector2>,
        size: CValue<raylib.interop.Vector2>,
        color: CValue<raylib.interop.Color>
    )

    fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>)
    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>)

    fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>)

    fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)
    fun drawRectangleLines(rectangle: CValue<Rectangle>, lineThick: Float, color: CValue<Color>)
}

fun BasicShapeDrawFunction(): BasicShapeDrawFunction {
    return DefaultBasicShapeDrawFunction()
}

private class DefaultBasicShapeDrawFunction() : BasicShapeDrawFunction {

    override fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>) {
        raylib.interop.DrawCircleV(center, radius, color)
    }

    override fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) {
        raylib.interop.DrawRectangle(x, y, width, height, color)
    }

    override fun drawRectangle(
        position: CValue<raylib.interop.Vector2>,
        size: CValue<raylib.interop.Vector2>,
        color: CValue<raylib.interop.Color>
    ) {
        raylib.interop.DrawRectangleV(position, size, color)
    }

    override fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>) {
        raylib.interop.DrawRectangleRec(rec, color)
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>) {
        raylib.interop.DrawLine(x1, y1, x2, y2, color)
    }

    override fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) {
        raylib.interop.DrawLineV(start, end, color)
    }

    override fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) {
        raylib.interop.DrawRectangleLines(x, y, width, height, color)
    }

    override fun drawRectangleLines(
        rectangle: CValue<Rectangle>,
        lineThick: Float,
        color: CValue<Color>
    ) {
        raylib.interop.DrawRectangleLinesEx(rectangle, lineThick, color)
    }
}
