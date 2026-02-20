package raylib.core

import kotlinx.cinterop.CValue

interface MouseFunction {
    val isCursorHidden: Boolean
    val mousePositionX: Int
    val mousePositionY: Int
    val mousePosition: CValue<Vector2>
    fun MouseButton.isPressed(): Boolean
    fun MouseButton.isDown(): Boolean

    fun MouseButton.isReleased(): Boolean

    fun MouseButton.isUp(): Boolean
    fun showCursor()
    fun hideCursor()

    val wheelMove: Float
    val wheelMoveVector: CValue<Vector2>
}

fun MouseFunction(): MouseFunction {
    return DefaultMouseFunction()
}

private class DefaultMouseFunction : MouseFunction {
    override val isCursorHidden: Boolean
        get() = raylib.interop.IsCursorHidden()
    override val mousePositionX: Int
        get() = raylib.interop.GetMouseX()
    override val mousePositionY: Int
        get() = raylib.interop.GetMouseX()
    override val mousePosition: CValue<Vector2>
        get() = raylib.interop.GetMousePosition()

    override fun MouseButton.isPressed() =
        raylib.interop.IsMouseButtonPressed(this.value.toInt())

    override fun MouseButton.isDown() =
        raylib.interop.IsMouseButtonDown(this.value.toInt())

    override fun MouseButton.isReleased() =
        raylib.interop.IsMouseButtonReleased(this.value.toInt())

    override fun MouseButton.isUp() =
        raylib.interop.IsMouseButtonUp(this.value.toInt())

    override fun showCursor() {
        raylib.interop.ShowCursor()
    }

    override fun hideCursor() {
        raylib.interop.HideCursor()
    }

    override val wheelMove: Float
        get() = raylib.interop.GetMouseWheelMove()
    override val wheelMoveVector: CValue<Vector2>
        get() = raylib.interop.GetMouseWheelMoveV()
}