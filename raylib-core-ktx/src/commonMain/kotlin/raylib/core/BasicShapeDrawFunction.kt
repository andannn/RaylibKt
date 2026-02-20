package raylib.core

import kotlinx.cinterop.CValue

interface BasicShapeDrawFunction {
    fun drawCircle(center: CValue<Vector2>, radius: Float, color: CValue<Color>)
}

fun BasicShapeDrawFunction(): BasicShapeDrawFunction {
    return DefaultBasicShapeDrawFunction()
}

private class DefaultBasicShapeDrawFunction() : BasicShapeDrawFunction {

    override fun drawCircle(
        center: CValue<Vector2>,
        radius: Float,
        color: CValue<Color>
    ) {
        raylib.interop.DrawCircleV(center, radius, color)
    }
}