/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Colors.DARKGREEN
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Matrix
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.matrixIdentity
import io.github.andannn.raylib.base.multiply
import io.github.andannn.raylib.base.rlMatrix
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.MutableState
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.RenderPhase
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.core.Vector2Alloc
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents

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

class Transform2DContext : Context {
    @PublishedApi
    internal var internalMatrix: CValue<Matrix>? = null
    val matrix: CValue<Matrix>
        get() = internalMatrix!!
}

class Transform2D(
    val position: Vector2,
    val scale: Vector2,
    val offset: Vector2,
    val angle: MutableState<Float>
)

fun RememberScope.Transform2DAlloc(
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    offset: CValue<Vector2> = Vector2(),
    angle: State<Float> = mutableStateOf(0f),
) =
    Transform2D(
        position = position.useContents { Vector2Alloc(x = x, y = y).value },
        scale = scale.useContents { Vector2Alloc(x = x, y = y).value },
        offset = offset.useContents { Vector2Alloc(x = x, y = y).value },
        angle = mutableStateOf(angle.value)
    )

/**
 * A persistent state container for 2D spatial transformations.
 *
 * This box holds stable pointers to C-interop structures, allowing child components
 * to modify the parent's transformation (position, scale, rotation, offset)
 * without triggering re-allocations or losing state between frames.
 *
 * @param transform Persistent state holding position, scale, rotation, and pivot offset.
 * @param key Debugging label used to identify this node within complex transformation hierarchies.
 * @param children Scoped closure for child components affected by this transformation.
 */
inline fun ComponentRegistry.transform2DComponent(
    key: Any,
    transform: Transform2D,
    crossinline children: ComponentScope.(Transform2D) -> Unit
) = component("Transform2D_$key") {
    val transform2DContext = remember {
        Transform2DContext()
    }
    when (find<WindowContext>().renderPhase) {
        RenderPhase.UPDATE -> {
            transform2DContext.internalMatrix = worldMatrix().multiply(transform.toMatrix())
            provide<Transform2DContext>(transform2DContext) {
                children(transform)
            }
        }

        RenderPhase.DRAW -> transform2DDrawInterceptor(transform) {
            children(transform)

            if (isDebug) {
                val offsetX = transform.offset.x.toInt()
                val offsetY = transform.offset.y.toInt()
                draw {
                    drawLine(-20, 0, 20, 0, DARKGREEN)
                    drawLine(0,  -20, 0, 20, DARKGREEN)
                    drawCircle(-offsetX, -offsetY, 2f, RED)
                }
            }
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
        val rad = angle.value * (kotlin.math.PI.toFloat() / 180f)
        val cosA = kotlin.math.cos(rad)
        val sinA = kotlin.math.sin(rad)

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
