package raylib.core

import kotlinx.cinterop.CValue

interface MouseFunction {
    val isCursorHidden: Boolean
    val mouseX: Int
    val mouseY: Int
    val mousePosition: CValue<Vector2>
    val mouseDelta: CValue<Vector2>
    fun MouseButton.isPressed(): Boolean
    fun MouseButton.isDown(): Boolean

    fun MouseButton.isReleased(): Boolean

    fun MouseButton.isUp(): Boolean
    fun showCursor()
    fun hideCursor()

    val mouseWheelMove: Float
    val mouseWheelMoveVector: CValue<Vector2>
}

fun MouseFunction(): MouseFunction {
    return DefaultMouseFunction()
}

private class DefaultMouseFunction : MouseFunction {
    override val isCursorHidden: Boolean
        get() = raylib.interop.IsCursorHidden()
    override val mouseX: Int
        get() = raylib.interop.GetMouseX()
    override val mouseY: Int
        get() = raylib.interop.GetMouseY()
    override val mousePosition: CValue<Vector2>
        get() = raylib.interop.GetMousePosition()
    override val mouseDelta: CValue<Vector2>
        get() = raylib.interop.GetMouseDelta()

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

    override val mouseWheelMove: Float
        get() = raylib.interop.GetMouseWheelMove()
    override val mouseWheelMoveVector: CValue<Vector2>
        get() = raylib.interop.GetMouseWheelMoveV()
}