package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.base.withWorldSpace
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.RememberScope
import io.github.andannn.raylib.core.State
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onSync
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import raylib.interop.Fade

class Spatial2DBoxState(
    val transform: Transform2D,
    val aabb: Aabb
)

fun RememberScope.Spatial2DBoxStateAlloc(
    position: CValue<Vector2> = Vector2(),
    scale: CValue<Vector2> = Vector2(1f, 1f),
    offset: CValue<Vector2> = Vector2(),
    angle: State<Float> = mutableStateOf(0f),
) = Spatial2DBoxState(
    transform = Transform2DAlloc(
        position = position,
        scale = scale,
        offset = offset,
        angle = angle
    ),
    aabb = AabbAlloc()
)

/**
 * A structural component that provides a 2D coordinate system and bounding box logic.
 *
 * @param state The persistent spatial state for this box.
 * @param size The local width and height of the box.
 * @param tag Identification tag for the component tree.
 * @param block Child components to be rendered within this box's coordinate space.
 */
inline fun ComponentRegistry.box2DComponent(
    state: Spatial2DBoxState,
    size: CValue<Vector2>,
    tag: String = "",
    crossinline block: ComponentRegistry.() -> Unit
) = transform2DComponent(state.transform, tag = tag) {
    component("") {
// TODO: draw when debug mode
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

        aabbComponent(state.aabb, size)
        onSync {
            findOrNull<CollisionCollectionContext>()?.register(state)
        }
        block()
    }
}
