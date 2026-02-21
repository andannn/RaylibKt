package raylib.core

import kotlinx.cinterop.CValue

interface BasicShapeDrawFunction {
    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>)
    fun drawRectangle(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>)
    fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>)

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

    override fun drawRectangle(rec: CValue<Rectangle>, color: CValue<Color>) {
        raylib.interop.DrawRectangleRec(rec, color)
    }
}
