package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Matrix
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.base.withWorldSpace
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.RenderPhase
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlin.math.abs

/**
 * Spatial State Carrier for 2D objects.
 * Encapsulates the core physical data required for an entity in the 2D world.
 */
class Spatial2D(
    val size: CValue<Vector2>,
    val transform: Transform2D,
    val aabb: Aabb,
)

fun Spatial2D.toGlobalRect() = aabb.toGlobalRect()

/**
 * Allocates a [Spatial2D] state within a [RememberScope].
 */
fun RememberScope.Spatial2DAlloc(
    size: CValue<Vector2>,
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    angle: State<Float> = mutableStateOf(0f),
    anchor: CValue<Vector2> = Anchor.TOP_LEFT,
): Spatial2D {
    val (anchorX, anchorY) = anchor.useContents { x to y }
    val offset = size.useContents {
        Vector2(-x * anchorX, -y * anchorY)
    }
    return Spatial2D(
        size = size,
        transform = Transform2DAlloc(
            position = position,
            scale = scale,
            offset = offset,
            angle = angle
        ),
        aabb = AabbAlloc(),
    )
}

object Anchor {
    val TOP_LEFT = Vector2(0.0f, 0.0f)
    val TOP_CENTER = Vector2(0.5f, 0.0f)
    val TOP_RIGHT = Vector2(1.0f, 0.0f)
    val CENTER_LEFT = Vector2(0.0f, 0.5f)
    val CENTER = Vector2(0.5f, 0.5f)
    val CENTER_RIGHT = Vector2(1.0f, 0.5f)
    val BOTTOM_LEFT = Vector2(0.0f, 1.0f)
    val BOTTOM_CENTER = Vector2(0.5f, 1.0f)
    val BOTTOM_RIGHT = Vector2(1.0f, 1.0f)
}

/**
 * Basic Spatial Component
 * Useful for objects that require a coordinate system but don't need
 * to be identified as a specific business entity (e.g., static decor).
 */
inline fun ComponentRegistry.spatial2DComponent(
    key: Any,
    size: CValue<Vector2>,
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    angle: State<Float> = mutableStateOf(0f),
    anchor: CValue<Vector2> = Anchor.TOP_LEFT,
    crossinline block: ComponentScope.(Spatial2D) -> Unit
) {
    val state = remember {
        Spatial2DAlloc(size = size, position = position, scale = scale, anchor = anchor, angle = angle)
    }
    spatial2DComponent(
        key = key,
        state = state,
        block = {
            block(state)
        },
    )
}

/**
 * A structural component that provides a 2D coordinate system and bounding box logic.
 *
 * @param key Identification tag for the component tree.
 * @param state The persistent spatial state for this box.
 * @param block Child components to be rendered within this box's coordinate space.
 */
inline fun ComponentRegistry.spatial2DComponent(
    key: Any,
    state: Spatial2D,
    crossinline block: ComponentScope.() -> Unit
) = transform2DComponent(key = key, state.transform) {
    if (find<WindowContext>().isDebug) {
        val debugRectColor = remember {
            randomColor()
        }
        val aabbRectColor = remember {
            randomColor()
        }
        onDraw {
            val (sizeX, sizeY) = state.size.useContents { x to y }
            drawRectangleLines(Rectangle(0f, 0f, sizeX, sizeY), 1f, debugRectColor)

            withWorldSpace {
                drawRectangleLines(state.aabb.toGlobalRect(), 1f, aabbRectColor)
            }
        }
    }

    aabbUpdate(state.aabb, state.size)
    block()
}

/**
 * A declarative component that synchronizes an [Aabb] state during the [RenderPhase.UPDATE] phase.
 *
 * @param aabb The target AABB to update.
 * @param size The local size of the object.
 */
@PublishedApi
internal fun ComponentRegistry.aabbUpdate(
    aabb: Aabb,
    size: CValue<Vector2>,
) {
    when (find<WindowContext>().renderPhase) {
        RenderPhase.UPDATE -> {
            val worldMatrix = worldMatrix()
            aabb.setFromLocalSize(size, worldMatrix)
        }

        else -> {}
    }
}

private fun RememberScope.AabbAlloc(): Aabb = nativeStateOf {
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

fun Aabb.toGlobalRect(): CValue<Rectangle> = Rectangle(
    x = min.x,
    y = min.y,
    width = max.x - min.x,
    height = max.y - min.y
)
