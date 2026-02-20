package raylib.core

import kotlinx.cinterop.CValue

interface TouchFunction {
    val touchPointCount: Int
    fun touchPosition(index: Int): CValue<Vector2>
}

fun TouchFunction(): TouchFunction {
    return DefaultTouchFunction()
}

private class DefaultTouchFunction : TouchFunction {
    override val touchPointCount: Int
        get() = raylib.interop.GetTouchPointCount()

    override fun touchPosition(index: Int): CValue<Vector2> {
        return raylib.interop.GetTouchPosition(index)
    }
}
