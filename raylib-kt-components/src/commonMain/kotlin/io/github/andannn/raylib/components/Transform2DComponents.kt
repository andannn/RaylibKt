/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.base.Matrix
import io.github.andannn.raylib.core.MutableState
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.core.RenderPhase
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.base.invert
import io.github.andannn.raylib.base.isCollisionWith
import io.github.andannn.raylib.base.matrixIdentity
import io.github.andannn.raylib.base.multiply
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.base.rlMatrix
import io.github.andannn.raylib.base.transform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


/**
 * Retrieves the current global transformation matrix for the calling component.
 *
 * If the component is nested within multiple [transform2DComponent] calls, this
 * method returns the cumulative result of all ancestor transformations
 * (calculated via matrix multiplication).
 *
 * @return The [CValue<Matrix>] representing the world-space transform.
 * - In a transformation flow: Returns the accumulated world matrix.
 * - At the root level: Returns the Identity Matrix [matrixIdentity], representing the origin.
 */
fun ContextProvider.worldMatrix(): CValue<Matrix> {
    return findOrNull<Transform2DContext>()?.matrix ?: matrixIdentity()
}

/**
 * Performs a hit test to determine if this screen-space point intersects a
 * rectangle defined in the component's local coordinate system.
 *
 * This method leverages the [ContextProvider] to retrieve the current world
 * transformation. It then projects the point from screen space into local space
 * using matrix inversion, making it capable of handling complex nested
 * translations, rotations, and scales.
 *
 * @param localRect The axis-aligned rectangle defined in local coordinates.
 * @return True if the point lies within the rectangle after local-space projection.
 */
context(contextProvider: ContextProvider)
fun CValue<Vector2>.hitTest(localRect: CValue<Rectangle>): Boolean {
    val world = contextProvider.worldMatrix()
    val invWorld = world.invert()
    val localPoint = this.transform(invWorld)
    return localPoint.isCollisionWith(localRect)
}

class Transform2DContext(
    val matrix: CValue<Matrix>
) : Context

class Transform2D(
    val position: Vector2,
    val scale: Vector2,
    val offset: Vector2,
    val angle: MutableState<Float>
)

/**
 * A persistent state container for 2D spatial transformations.
 *
 * This box holds stable pointers to C-interop structures, allowing child components
 * to modify the parent's transformation (position, scale, rotation, offset)
 * without triggering re-allocations or losing state between frames.
 *
 * @property position The 2D translation vector in parent/world space.
 * @property scale The scaling factor (1.0 = original size).
 * @property offset The local pivot offset. Affects the center of rotation and scaling.
 * @property angle A [MutableState] holding the rotation angle in degrees.
 */
inline fun ComponentRegistry.transform2DComponent(
    position: Vector2? = null,
    scale: Vector2? = null,
    offset: Vector2? = null,
    angle: MutableState<Float>? = null,
    tag: String = "",
    crossinline children: ComponentRegistry.(Transform2D) -> Unit
) = component("Transform2D_$tag") {
    val positionV = position ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val scaleV = scale ?: remember { nativeStateOf { Vector2Alloc(1f, 1f) } }.value
    val offsetV = offset ?: remember { nativeStateOf { Vector2Alloc() } }.value
    val angle = angle ?: remember { mutableStateOf(0f) }

    val transform2DState = remember {
        Transform2D(positionV, scaleV, offsetV, angle)
    }

    when (find<WindowContext>().renderPhase) {
        RenderPhase.UPDATE -> provide<Transform2DContext>(
            Transform2DContext(
                worldMatrix()
                    .multiply(transform2DState.toMatrix())
            )
        ) {
            children(transform2DState)
        }

        RenderPhase.DRAW -> transform2DDrawInterceptor(transform2DState) {
            children(transform2DState)
        }
    }
}

inline fun transform2DDrawInterceptor(
    transform2DState: Transform2D,
    crossinline block: (CValue<Matrix>) -> Unit
): CValue<Matrix> {
    val transform = transform2DState.toMatrix()

    rlMatrix {
        multMatrix(transform)
        block(transform)
    }

    return transform
}

fun Transform2D.toMatrix(): CValue<Matrix> {
    val transform2DState: Transform2D = this
    val position: Vector2 = transform2DState.position
    val angle: State<Float> = transform2DState.angle
    val scale: Vector2 = transform2DState.scale
    val offset: Vector2 = transform2DState.offset
    val transform = cValue<Matrix>().useContents {
        val matrix = this
        val rad = angle.value * (PI.toFloat() / 180f)
        val cosA = cos(rad)
        val sinA = sin(rad)

        val m = matrix.apply {
            m0 = cosA * scale.x
            m1 = sinA * scale.x
            m4 = -sinA * scale.y
            m5 = cosA * scale.y
            m10 = 1f
            m15 = 1f
            m12 = position.x + m0 * offset.x + m4 * offset.y
            m13 = position.y + m1 * offset.x + m5 * offset.y
        }
        m.readValue()
    }
    return transform
}
