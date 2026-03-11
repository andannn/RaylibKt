package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.base.withWorldSpace
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import raylib.interop.Fade

/**
 * Spatial State Carrier for 2D objects.
 * Encapsulates the core physical data required for an entity in the 2D world.
 */
class Positional2D(
    val size: CValue<Vector2>,
    val transform: Transform2D,
    val aabb: Aabb,
)

interface Positional2DEntity : Entity {
    val state: Positional2D
}

/**
 * Convenience extension to get the current AABB as a Raylib Rectangle.
 */
fun Positional2DEntity.getRect() = state.aabb.toRect()

/**
 * Allocates a [Positional2D] state within a [RememberScope].
 */
fun RememberScope.positional2DAlloc(
    size: CValue<Vector2>,
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    offset: CValue<Vector2> = Vector2(),
    angle: State<Float> = mutableStateOf(0f),
) = Positional2D(
    size = size,
    transform = Transform2DAlloc(
        position = position,
        scale = scale,
        offset = offset,
        angle = angle
    ),
    aabb = AabbAlloc(),
)

/**
 * Entity-specific Spatial Component.
 * Mounts a [Positional2DEntity] into the component tree and handles its
 * automatic registration into the spatial grid every frame.
 *
 * @param positional2DEntity The business entity instance to be synced.
 */
inline fun ComponentRegistry.positional2DEntityComponent(
    positional2DEntity: Positional2DEntity,
    tag: String = "",
    crossinline block: ComponentRegistry.() -> Unit
) = positional2DComponent(
    positional2DEntity.state,
    positional2DEntity.state.size,
    tag,
) {
    component("") {
        onUpdate {
            findOrNull<Collision2DContext>()?.register(positional2DEntity)
        }

        block()
    }
}

/**
 * Basic Spatial Component (Auto-allocating version).
 * Useful for objects that require a coordinate system but don't need
 * to be identified as a specific business entity (e.g., static decor).
 */
inline fun ComponentRegistry.positional2DComponent(
    size: CValue<Vector2>,
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    offset: CValue<Vector2> = Vector2(),
    angle: State<Float> = mutableStateOf(0f),
    tag: String = "",
    crossinline block: ComponentRegistry.() -> Unit
) {
    val state = remember {
        positional2DAlloc(size = size, position = position, scale = scale, offset = offset, angle = angle)
    }
    positional2DComponent(
        state = state,
        size = size,
        tag = tag, block = block,
    )
}

/**
 * A structural component that provides a 2D coordinate system and bounding box logic.
 *
 * @param state The persistent spatial state for this box.
 * @param size The local width and height of the box.
 * @param tag Identification tag for the component tree.
 * @param block Child components to be rendered within this box's coordinate space.
 */
inline fun ComponentRegistry.positional2DComponent(
    state: Positional2D,
    size: CValue<Vector2>,
    tag: String = "",
    crossinline block: ComponentRegistry.() -> Unit
) = transform2DComponent(state.transform, tag = tag) {
    component("") {
        if (find<WindowContext>().isDebug) {
            val debugRectColor = remember {
                randomColor()
            }
            val aabbRectColor = remember {
                randomColor()
            }
            onDraw {
                drawRectangle(Vector2(), size, Fade(debugRectColor, 0.6f))

                withWorldSpace {
                    drawRectangle(state.aabb.toRect(), Fade(aabbRectColor, 0.6f))
                }
            }
        }

        aabbComponent(state.aabb, size)
        block()
    }
}
