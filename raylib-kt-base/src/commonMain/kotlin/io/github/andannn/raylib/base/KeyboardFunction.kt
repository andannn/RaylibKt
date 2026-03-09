/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import raylib.interop.IsKeyDown
import raylib.interop.IsKeyPressed
import raylib.interop.IsKeyPressedRepeat
import raylib.interop.IsKeyReleased
import raylib.interop.IsKeyUp

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
        IsKeyDown(this.value.toInt())

    override fun KeyboardKey.isUp() =
        IsKeyUp(this.value.toInt())

    override fun KeyboardKey.isPressed() =
        IsKeyPressed(this.value.toInt())

    override fun KeyboardKey.isReleased() =
        IsKeyReleased(this.value.toInt())

    override fun KeyboardKey.isPressedRepeat() =
        IsKeyPressedRepeat(this.value.toInt())
}