/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import raylib.interop.IsGamepadButtonDown
import raylib.interop.IsGamepadButtonPressed
import raylib.interop.IsGamepadButtonReleased

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
        return IsGamepadButtonDown(index, this.value.toInt())
    }

    override fun GamepadButton.isPressed(index: Int): Boolean {
        return IsGamepadButtonPressed(index, this.value.toInt())
    }

    override fun GamepadButton.isReleased(index: Int): Boolean {
        return IsGamepadButtonReleased(index, this.value.toInt())
    }
}