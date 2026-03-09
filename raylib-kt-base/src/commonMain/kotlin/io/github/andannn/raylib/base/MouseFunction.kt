/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import raylib.interop.GetMouseDelta
import raylib.interop.GetMousePosition
import raylib.interop.GetMouseWheelMove
import raylib.interop.GetMouseWheelMoveV
import raylib.interop.GetMouseX
import raylib.interop.GetMouseY
import raylib.interop.HideCursor
import raylib.interop.IsCursorHidden
import raylib.interop.IsMouseButtonDown
import raylib.interop.IsMouseButtonPressed
import raylib.interop.IsMouseButtonReleased
import raylib.interop.IsMouseButtonUp
import raylib.interop.ShowCursor

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
        get() = IsCursorHidden()
    override val mouseX: Int
        get() = GetMouseX()
    override val mouseY: Int
        get() = GetMouseY()
    override val mousePosition: CValue<Vector2>
        get() = GetMousePosition()
    override val mouseDelta: CValue<Vector2>
        get() = GetMouseDelta()

    override fun MouseButton.isPressed() =
        IsMouseButtonPressed(this.value.toInt())

    override fun MouseButton.isDown() =
        IsMouseButtonDown(this.value.toInt())

    override fun MouseButton.isReleased() =
        IsMouseButtonReleased(this.value.toInt())

    override fun MouseButton.isUp() =
        IsMouseButtonUp(this.value.toInt())

    override fun showCursor() {
        ShowCursor()
    }

    override fun hideCursor() {
        HideCursor()
    }

    override val mouseWheelMove: Float
        get() = GetMouseWheelMove()
    override val mouseWheelMoveVector: CValue<Vector2>
        get() = GetMouseWheelMoveV()
}