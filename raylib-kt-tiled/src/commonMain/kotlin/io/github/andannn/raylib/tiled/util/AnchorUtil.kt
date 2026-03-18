/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.util

import io.github.andannn.raylib.components.Anchor
import io.github.andannn.raylib.tiled.model.ObjectAlignment

fun ObjectAlignment.toRayAnchor() = when (this) {
    ObjectAlignment.UNSPECIFIED -> Anchor.BOTTOM_LEFT
    ObjectAlignment.TOP_LEFT -> Anchor.TOP_LEFT
    ObjectAlignment.TOP -> Anchor.TOP_CENTER
    ObjectAlignment.TOP_RIGHT -> Anchor.TOP_RIGHT
    ObjectAlignment.LEFT -> Anchor.LEFT_CENTER
    ObjectAlignment.CENTER -> Anchor.CENTER
    ObjectAlignment.RIGHT -> Anchor.RIGHT_CENTER
    ObjectAlignment.BOTTOM_LEFT -> Anchor.BOTTOM_LEFT
    ObjectAlignment.BOTTOM -> Anchor.BOTTOM_CENTER
    ObjectAlignment.BOTTOM_RIGHT -> Anchor.BOTTOM_RIGHT
}