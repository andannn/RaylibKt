/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import raylib.interop.BeginMode2D
import raylib.interop.BeginScissorMode
import raylib.interop.EndMode2D
import raylib.interop.EndScissorMode


inline fun mode2d(camera: Camera2D, crossinline block: () -> Unit) {
    mode2d(camera.readValue(), block)
}

inline fun mode2d(camera: CValue<Camera2D>, crossinline block: () -> Unit) {
    BeginMode2D(camera)
    block()
    EndMode2D()
}

inline fun scissorMode(rectangle: Rectangle, enabled: Boolean = true, crossinline block: () -> Unit) {
    scissorMode(
        rectangle.x.toInt(),
        rectangle.y.toInt(),
        rectangle.width.toInt(),
        rectangle.height.toInt(),
        enabled,
        block
    )
}

inline fun scissorMode(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    enabled: Boolean = true,
    crossinline block: () -> Unit
) {
    if (enabled) BeginScissorMode(x, y, width, height)
    block()
    if (enabled) EndScissorMode()
}