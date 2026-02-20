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
}

fun MouseFunction(): MouseFunction {
    return DefaultMouseFunction()
}

private class DefaultMouseFunction : MouseFunction {
    override val isCursorHidden: Boolean
        get() = RlCursor.isHidden()
    override val mousePositionX: Int
        get() = RlMouse.x()
    override val mousePositionY: Int
        get() = RlMouse.y()
    override val mousePosition: CValue<Vector2>
        get() = RlMouse.position()

    override fun MouseButton.isPressed() =
        RlMouse.isButtonPressed(this.value.toInt())

    override fun MouseButton.isDown() =
        RlMouse.isButtonDown(this.value.toInt())

    override fun MouseButton.isReleased() =
        RlMouse.isButtonReleased(this.value.toInt())

    override fun MouseButton.isUp() =
        RlMouse.isButtonUp(this.value.toInt())

    override fun showCursor() {
        RlCursor.show()
    }

    override fun hideCursor() {
        RlCursor.hide()
    }
}