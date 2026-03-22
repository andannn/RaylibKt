/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.DisposableRegistry
import io.github.andannn.raylib.runtime.State
import io.github.andannn.raylib.runtime.nativeStateOf
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.alloc

fun DisposableRegistry.Vector2Alloc(x: Float = 0f, y: Float = 0f): State<Vector2> =
    nativeStateOf {
        this.allocVector2(x, y)
    }

fun DisposableRegistry.RectangleAlloc(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f
): State<Rectangle> =
    nativeStateOf {
        this.allocRectangle(x, y, width, height)
    }

private fun NativePlacement.allocVector2(x: Float = 0f, y: Float = 0f) = alloc<Vector2>().apply {
    this.x = x
    this.y = y
}

private fun NativePlacement.allocRectangle(
    x: Float = 0f,
    y: Float = 0f,
    width: Float = 0f,
    height: Float = 0f
) = alloc<Rectangle>().apply {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}
