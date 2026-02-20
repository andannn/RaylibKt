package raylib.core

interface KeyboardFunction {
    fun KeyboardKey.isDown(): Boolean
    fun KeyboardKey.isUp(): Boolean
    fun KeyboardKey.isPressed(): Boolean
    fun KeyboardKey.isReleased(): Boolean
    fun KeyboardKey.isPressedRepeat(): Boolean
}

fun KeyboardFunction(): KeyboardFunction {
    return DefaultKeyboardFunction()
}

private class DefaultKeyboardFunction : KeyboardFunction {
    override fun KeyboardKey.isDown() =
        raylib.interop.IsKeyDown(this.value.toInt())

    override fun KeyboardKey.isUp() =
        raylib.interop.IsKeyUp(this.value.toInt())

    override fun KeyboardKey.isPressed() =
        raylib.interop.IsKeyPressed(this.value.toInt())

    override fun KeyboardKey.isReleased() =
        raylib.interop.IsKeyReleased(this.value.toInt())

    override fun KeyboardKey.isPressedRepeat() =
        raylib.interop.IsKeyPressedRepeat(this.value.toInt())
}