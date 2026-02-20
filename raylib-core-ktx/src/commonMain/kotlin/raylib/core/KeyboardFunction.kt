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
        RlKeyboard.isKeyDown(this.value.toInt())

    override fun KeyboardKey.isUp() =
        RlKeyboard.isKeyUp(this.value.toInt())

    override fun KeyboardKey.isPressed() =
        RlKeyboard.isKeyPressed(this.value.toInt())

    override fun KeyboardKey.isReleased() =
        RlKeyboard.isKeyReleased(this.value.toInt())

    override fun KeyboardKey.isPressedRepeat() =
        RlKeyboard.isKeyPressedRepeat(this.value.toInt())
}