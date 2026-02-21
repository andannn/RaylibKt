package raylib.core

import kotlinx.cinterop.CValue

interface GestureFunction {
    val touchPointCount: Int
    fun touchPosition(index: Int): CValue<Vector2>

    val gestureDetected: Gesture
}

fun TouchFunction(): GestureFunction {
    return DefaultTouchFunction()
}

private class DefaultTouchFunction : GestureFunction {
    override val touchPointCount: Int
        get() = raylib.interop.GetTouchPointCount()

    override fun touchPosition(index: Int): CValue<Vector2> {
        return raylib.interop.GetTouchPosition(index)
    }

    override val gestureDetected: Gesture
        get() = gestureByValue(raylib.interop.GetGestureDetected()) ?: error("Unknown gesture")
}

private fun gestureByValue(value: Int): Gesture? =
    Gesture.entries.find { it.value.toInt() == value }