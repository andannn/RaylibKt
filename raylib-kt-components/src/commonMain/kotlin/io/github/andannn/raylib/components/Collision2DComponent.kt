package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.GameContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlin.math.floor

inline fun ComponentRegistry.collision2DComponent(
    tag: String = "",
    cellSize: Int,
    crossinline block: ComponentRegistry.() -> Unit
) = component(tag) {
    val context = remember {
        Collision2DContext(cellSize)
    }
    provide(context) {
        block()
    }

    context.clear()
}

inline fun ComponentRegistry.hitbox2DComponent(
    positional2DIdentity: Positional2DIdentity,
    size: CValue<Vector2>,
    tag: String = "",
    crossinline block: ComponentRegistry.() -> Unit
) = positional2DComponent(
    positional2DIdentity.state,
    size,
    tag,
) {
    component("") {
        onUpdate {
            findOrNull<Collision2DContext>()?.register(positional2DIdentity)
        }

        block()
    }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Positional2D.queryNearby(crossinline block: (Positional2DIdentity) -> Unit) {
    contextProvider.findOrNull<Collision2DContext>()?.queryInRect(this.aabb.toRect())?.forEach(block)
}

class Collision2DContext(
    private val cellSize: Int
) : Context {
    internal val cells = mutableMapOf<Long, MutableList<Positional2DIdentity>>()

    fun register(identity: Positional2DIdentity) {
        val aabb = identity.state.aabb
        val xStart = floor(aabb.min.x / cellSize).toInt()
        val xEnd = floor(aabb.max.x / cellSize).toInt()
        val yStart = floor(aabb.min.y / cellSize).toInt()
        val yEnd = floor(aabb.max.y / cellSize).toInt()

        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells.getOrPut(getCellKeyHash(x, y)) { mutableListOf() }.add(identity)
            }
        }
    }

    fun queryInRect(
        rect: CValue<Rectangle>,
    ): Iterable<Positional2DIdentity> = rect.useContents {
        val rangeRect = this
        val xStart = floor(rangeRect.x / cellSize).toInt()
        val xEnd = floor((rangeRect.x + rangeRect.width) / cellSize).toInt()
        val yStart = floor(rangeRect.y / cellSize).toInt()
        val yEnd = floor((rangeRect.y + rangeRect.height) / cellSize).toInt()

        val result = mutableSetOf<Positional2DIdentity>()
        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells[getCellKeyHash(x, y)]?.let { result.addAll(it) }
            }
        }
        return result
    }

    fun clear() {
        cells.clear()
    }

    private fun getCellKeyHash(x: Int, y: Int): Long {
        return (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)
    }
}
