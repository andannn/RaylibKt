package raylib.core

import kotlinx.cinterop.CValue

interface GestureFunction {
    val touchPointCount: Int
    fun touchPosition(index: Int): CValue<Vector2>

    fun touchPositions(): Sequence<CValue<Vector2>>

    fun Gesture.isDetected(): Boolean
    val gestureDetected: Gesture
}

fun GestureFunction(): GestureFunction {
    return DefaultTouchFunction()
}

private class DefaultTouchFunction : GestureFunction {
    override val touchPointCount: Int
        get() = raylib.interop.GetTouchPointCount()

    override fun touchPosition(index: Int): CValue<Vector2> {
        return raylib.interop.GetTouchPosition(index)
    }

    override fun touchPositions(): Sequence<CValue<Vector2>> = sequence {
        repeat(touchPointCount) { index ->
            yield(touchPosition(index))
        }
    }

    override fun Gesture.isDetected(): Boolean {
        return raylib.interop.IsGestureDetected(value)
    }

    override val gestureDetected: Gesture
        get() = gestureByValue(raylib.interop.GetGestureDetected()) ?: error("Unknown gesture")
}

private fun gestureByValue(value: Int): Gesture? =
    Gesture.entries.find { it.value.toInt() == value }