/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.interop.GetScreenToWorld2D
import raylib.interop.GetWorldToScreen2D

fun Camera2D.screenToWorldPosition(screenPosition: CValue<Vector2>): CValue<Vector2> {
   return readValue().screenToWorldPosition(screenPosition)
}

fun CValue<Camera2D>.screenToWorldPosition(screenPosition: CValue<Vector2>): CValue<Vector2> {
    return GetScreenToWorld2D(screenPosition, this)
}

fun Camera2D.worldToScreenPosition(worldPositon: CValue<Vector2>): CValue<Vector2> {
   return readValue().worldToScreenPosition(worldPositon)
}

fun CValue<Camera2D>.worldToScreenPosition(worldPositon: CValue<Vector2>): CValue<Vector2> {
    return GetWorldToScreen2D(worldPositon, this)
}

fun Camera2D.setTarget(x: Float, y: Float) {
    target.x = x
    target.y = y
}

fun Camera2D.setTarget(target: CValue<Vector2>) {
    val camera = this
    target.useContents {
        camera.target.x = x
        camera.target.y = y
    }
}

fun Camera2D.setOffset(x: Float, y: Float) {
    offset.x = x
    offset.y = y
}


fun Camera2D.setOffset(offset: CValue<Vector2>) {
    val camera = this
    offset.useContents {
        camera.offset.x = x
        camera.offset.y = y
    }
}
