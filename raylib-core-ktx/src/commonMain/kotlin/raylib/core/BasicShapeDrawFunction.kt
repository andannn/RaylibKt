package raylib.core

import kotlinx.cinterop.CValue

interface BasicShapeDrawFunction {
    fun drawCircle(centerX: Int, centerY: Int, radius: Float, color: CValue<Color>)

    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>)

    fun drawCircleGradient(x: Int, y: Int, radius: Float, color1: CValue<Color>, color2: CValue<Color>)

    fun drawCircleLines(x: Int, y: Int, radius: Float, color: CValue<Color>)
    fun drawCircleLines(center: CValue<Vector2>, radius: Float, color: CValue<Color>)
    fun drawEllipse(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>)
    fun drawEllipseLines(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>)
    fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)
    fun drawRectangle(
        position: CValue<raylib.interop.Vector2>,
        size: CValue<raylib.interop.Vector2>,
        color: CValue<raylib.interop.Color>
    )
    fun drawRectangle(
        rectangle: CValue<Rectangle>,
        origin: CValue<raylib.interop.Vector2>,
        rotation: Float,
        color: CValue<raylib.interop.Color>
    )

    fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>)
    fun drawRectangleGradientH(x: Int, y: Int, width: Int, height: Int, color1: CValue<Color>, color2: CValue<Color>)

    fun drawRectangleLines(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)

    fun drawRectangleLines(rectangle: CValue<Rectangle>, lineThick: Float, color: CValue<Color>)

    fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>)

    fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>)

    fun drawTriangle(v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>)

    fun drawTriangleLines(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    )

    fun drawPoly(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    )

    fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    )

    fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        lineThick: Float,
        color: CValue<Color>
    )

    fun drawLineBezier(
        start: CValue<Vector2>,
        end: CValue<Vector2>,
        thick: Float,
        color: CValue<Color>
    )
}


fun BasicShapeDrawFunction(): BasicShapeDrawFunction {
    return DefaultBasicShapeDrawFunction()
}

private class DefaultBasicShapeDrawFunction() : BasicShapeDrawFunction {
    override fun drawCircleGradient(
        x: Int,
        y: Int,
        radius: Float,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) {
        raylib.interop.DrawCircleGradient(x, y, radius, color1, color2)
    }

    override fun drawCircleLines(x: Int, y: Int, radius: Float, color: CValue<Color>) {
        raylib.interop.DrawCircleLines(x, y, radius, color)
    }

    override fun drawCircleLines(center: CValue<Vector2>, radius: Float, color: CValue<Color>) {
        raylib.interop.DrawCircleLinesV(center, radius, color)
    }

    override fun drawEllipse(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) {
        raylib.interop.DrawEllipse(x, y, radiusH, radiusV, color)
    }

    override fun drawEllipseLines(x: Int, y: Int, radiusH: Float, radiusV: Float, color: CValue<Color>) {
        raylib.interop.DrawEllipseLines(x, y, radiusH, radiusV, color)
    }

    override fun drawCircle(centerX: Int, centerY: Int, radius: Float, color: CValue<Color>) {
        drawCircle(Vector2(centerX.toFloat(), centerY.toFloat()), radius, color)
    }

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

    override fun drawRectangle(
        rectangle: CValue<Rectangle>,
        origin: CValue<raylib.interop.Vector2>,
        rotation: Float,
        color: CValue<raylib.interop.Color>
    ) {
        raylib.interop.DrawRectanglePro(rectangle, origin, rotation, color)
    }

    override fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>) {
        raylib.interop.DrawRectangleRec(rec, color)
    }

    override fun drawRectangleGradientH(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color1: CValue<Color>,
        color2: CValue<Color>
    ) {
        raylib.interop.DrawRectangleGradientH(x, y, width, height, color1, color2)
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: CValue<Color>) {
        raylib.interop.DrawLine(x1, y1, x2, y2, color)
    }

    override fun drawLine(start: CValue<Vector2>, end: CValue<Vector2>, color: CValue<Color>) {
        raylib.interop.DrawLineV(start, end, color)
    }

    override fun drawTriangle(v1: CValue<Vector2>, v2: CValue<Vector2>, v3: CValue<Vector2>, color: CValue<Color>) {
        raylib.interop.DrawTriangle(v1, v2, v3, color)
    }

    override fun drawTriangleLines(
        v1: CValue<Vector2>,
        v2: CValue<Vector2>,
        v3: CValue<Vector2>,
        color: CValue<Color>
    ) {
        raylib.interop.DrawTriangleLines(v1, v2, v3, color)
    }

    override fun drawPoly(center: CValue<Vector2>, sides: Int, radius: Float, rotation: Float, color: CValue<Color>) {
        raylib.interop.DrawPoly(center, sides, radius, rotation, color)
    }

    override fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        color: CValue<Color>
    ) {
        raylib.interop.DrawPolyLines(center, sides, radius, rotation, color)
    }

    override fun drawPolyLines(
        center: CValue<Vector2>,
        sides: Int,
        radius: Float,
        rotation: Float,
        lineThick: Float,
        color: CValue<Color>
    ) {
        raylib.interop.DrawPolyLinesEx(center, sides, radius, rotation, lineThick, color)
    }

    override fun drawLineBezier(start: CValue<Vector2>, end: CValue<Vector2>, thick: Float, color: CValue<Color>) {
        raylib.interop.DrawLineBezier(start, end, thick, color)
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
