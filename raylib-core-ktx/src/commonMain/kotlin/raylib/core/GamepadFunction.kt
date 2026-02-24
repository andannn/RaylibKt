package raylib.core

interface GamepadFunction {
    fun GamepadButton.isDown(index: Int): Boolean
    fun GamepadButton.isPressed(index: Int): Boolean
    fun GamepadButton.isReleased(index: Int): Boolean
}

fun GamepadFunction(): GamepadFunction {
    return DefaultGamepadButtonFunction()
}

private class DefaultGamepadButtonFunction : GamepadFunction {
    override fun GamepadButton.isDown(index: Int): Boolean {
        return raylib.interop.IsGamepadButtonDown(index, this.value.toInt())
    }

    override fun GamepadButton.isPressed(index: Int): Boolean {
        return raylib.interop.IsGamepadButtonPressed(index, this.value.toInt())
    }

    override fun GamepadButton.isReleased(index: Int): Boolean {
        return raylib.interop.IsGamepadButtonReleased(index, this.value.toInt())
    }
}