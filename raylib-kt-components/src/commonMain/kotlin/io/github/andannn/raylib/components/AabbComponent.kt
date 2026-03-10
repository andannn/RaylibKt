/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Matrix
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.RenderPhase
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.nativeStateOf
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlin.math.abs

/**
 * A declarative component that synchronizes an [Aabb] state during the [RenderPhase.UPDATE] phase.
 *
 * @param aabb The target AABB to update.
 * @param size The local size of the object.
 * @param tag Optional tag for debugging within the component tree.
 */
fun ComponentRegistry.aabbComponent(
    aabb: Aabb,
    size: CValue<Vector2>,
    tag: String = "",
) {
    component("boundsComponent_$tag") {
        when (find<WindowContext>().renderPhase) {
            RenderPhase.UPDATE -> {
                val worldMatrix = worldMatrix()
                aabb.setFromLocalSize(size, worldMatrix)
            }

            else -> {}
        }
    }
}

fun RememberScope.AabbAlloc(): Aabb = nativeStateOf {
    Aabb(
        Vector2Alloc(),
        Vector2Alloc(),
    )
}.value

/**
 * An Axis-Aligned Bounding Box (AABB) in world space coordinates.
 * Used for broad-phase collision detection, visibility culling, and UI layout.
 * The box is defined by two points, [min] and [max], and is always aligned
 * with the world axes (no rotation).
 */
class Aabb(
    val min: Vector2,
    val max: Vector2,
) {
    internal fun setFromLocalSize(size: CValue<Vector2>, worldMatrix: CValue<Matrix>) = worldMatrix.useContents {
        val (sizeX, sizeY) = size.useContents { x to y }
        val worldMatrix = this
        // 1. Half-extents
        val hx = sizeX / 2f
        val hy = sizeY / 2f

        // 2. Calculate center point in world coordinate
        val centerX = worldMatrix.m12 + (worldMatrix.m0 * hx + worldMatrix.m4 * hy)
        val centerY = worldMatrix.m13 + (worldMatrix.m1 * hx + worldMatrix.m5 * hy)

        // 3. Absolute Rotate
        val worldHx = abs(worldMatrix.m0) * hx + abs(worldMatrix.m4) * hy
        val worldHy = abs(worldMatrix.m1) * hx + abs(worldMatrix.m5) * hy

        min.x = centerX - worldHx
        min.y = centerY - worldHy
        max.x = centerX + worldHx
        max.y = centerY + worldHy
    }
}

fun Aabb.toRect(): CValue<Rectangle> = Rectangle(
    x = min.x,
    y = min.y,
    width = max.x - min.x,
    height = max.y - min.y
)
