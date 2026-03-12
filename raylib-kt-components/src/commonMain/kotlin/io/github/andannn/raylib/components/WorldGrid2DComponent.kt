package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.GameContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.findOrNull
import io.github.andannn.raylib.core.provide
import io.github.andannn.raylib.core.remember
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlin.math.floor

/**
 * Provides a 2D spatial partitioning environment for the child component tree.
 *
 * @param key Identification tag for the context component.
 * @param cellSize Dimensions of a single grid square. Optimization depends on this value
 *                 matching the average entity size.
 * @param block Subtree where spatial queries and registrations will occur.
 */
inline fun ComponentRegistry.world2DGridComponent(
    key: Any,
    cellSize: Int,
    crossinline block: ComponentRegistry.() -> Unit
) = component(key) {
    val context = remember {
        WorldGrid2DContext(cellSize)
    }
    provide(context) {
        block()
    }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Positional2D.queryNearby(crossinline block: (Positional2DEntity) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toRect())?.forEach(block)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Positional2DEntity> Positional2D.queryNearby(crossinline block: (T) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toRect())?.filterIsInstance<T>()
        ?.forEach(block)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Positional2D.queryNearbyUntil(crossinline block: (Positional2DEntity) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toRect())?.any { block(it) }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Positional2DEntity> Positional2D.queryNearbyUntil(crossinline block: (T) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toRect())?.filterIsInstance<T>()
        ?.any { block(it) }
}

context(_: GameContext)
inline fun ContextProvider.queryAll(crossinline block: (Positional2DEntity) -> Unit) {
    findOrNull<WorldGrid2DContext>()?.queryAll()?.forEach(block)
}

context(_: GameContext)
inline fun <reified T : Positional2DEntity> ContextProvider.queryAll(crossinline block: (T) -> Unit) {
    findOrNull<WorldGrid2DContext>()?.queryAll()?.filterIsInstance<T>()
        ?.forEach(block)
}

context(_: GameContext)
inline fun ContextProvider.queryAllUntil(crossinline block: (Positional2DEntity) -> Boolean) {
    val _ = findOrNull<WorldGrid2DContext>()?.queryAll()?.any { block(it) }
}

context(_: GameContext)
inline fun <reified T : Positional2DEntity> ContextProvider.queryAllUntil(crossinline block: (T) -> Boolean) {
    val _ = findOrNull<WorldGrid2DContext>()?.queryAll()?.filterIsInstance<T>()
        ?.any { block(it) }
}

/**
 * For 2D spatial partitioning.
 * Maps world coordinates to discrete grid cells to optimize broad-phase collision detection.
 */
class WorldGrid2DContext(
    private val cellSize: Int,
) : Context {
    internal val cells = mutableMapOf<Long, MutableList<Positional2DEntity>>()
    private val allEntities = mutableListOf<Positional2DEntity>()

    fun register(identity: Positional2DEntity) {
        allEntities.add(identity)

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

    fun queryAll(): Iterable<Positional2DEntity> = allEntities

    fun queryInRect(
        rect: CValue<Rectangle>,
    ): Iterable<Positional2DEntity> = rect.useContents {
        inflate(cellSize / 2f)

        val xStart = floor(x / cellSize).toInt()
        val xEnd = floor((x + width) / cellSize).toInt()
        val yStart = floor(y / cellSize).toInt()
        val yEnd = floor((y + height) / cellSize).toInt()

        val result = mutableSetOf<Positional2DEntity>()
        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells[getCellKeyHash(x, y)]?.let { result.addAll(it) }
            }
        }
        return result
    }

    fun clear() {
        allEntities.clear()
        cells.values.forEach {
            it.clear()
        }
    }

    fun remove(identity: Positional2DEntity) {
        allEntities.remove(identity)
        val _ = cells.values.any { it.remove(identity) }
    }

    private fun getCellKeyHash(x: Int, y: Int): Long {
        return (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)
    }
}

private fun Rectangle.inflate(amount: Float) {
    this.x -= amount
    this.y -= amount
    this.width += (amount * 2f)
    this.height += (amount * 2f)
}