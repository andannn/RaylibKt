package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextProvider
import io.github.andannn.raylib.core.GameContext
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.doOnce
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
    crossinline block: ComponentScope.() -> Unit
) = component(key) {
    val context = remember {
        WorldGrid2DContext(cellSize)
    }
    provide(context) {
        block()
    }
}

fun ComponentRegistry.registerEntityToWorldGrid2D(
    entity: Entity,
    position: Positional2D,
    extra: Any? = null
) {
    doOnce {
        val contextOrNull = findOrNull<WorldGrid2DContext>()
        val entity = PositionalEntity(entity, position, extra)
        contextOrNull?.register(entity)

        disposeOnClose {
            contextOrNull?.remove(identity = entity)
        }
    }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Positional2D.queryNearby(crossinline block: (PositionalEntity) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())?.forEach(block)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Positional2D.queryNearby(crossinline block: (T, Positional2D, Any?) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())
        ?.filter { (entity, _, _) -> entity is T }
        ?.forEach {
            block(it.entity as T, it.position, it.extra)
        }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Positional2D.queryNearbyUntil(crossinline block: (PositionalEntity) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())?.any { block(it) }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Positional2D.queryNearbyUntil(crossinline block: (T, Positional2D, Any?) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())
        ?.filter { (entity, _, _) -> entity is T }
        ?.any {
            block(it.entity as T, it.position, it.extra)
        }
}

data class PositionalEntity(
    val entity: Entity,
    val position: Positional2D,
    val extra: Any? = null
)

/**
 * For 2D spatial partitioning.
 * Maps world coordinates to discrete grid cells to optimize broad-phase collision detection.
 */
class WorldGrid2DContext(
    private val cellSize: Int,
) : Context {
    private val cells = mutableMapOf<Long, MutableList<PositionalEntity>>()
    private val allEntities = mutableListOf<PositionalEntity>()

    fun register(positionalEntity: PositionalEntity) {
        allEntities.add(positionalEntity)

        val aabb = positionalEntity.position.aabb
        val xStart = floor(aabb.min.x / cellSize).toInt()
        val xEnd = floor(aabb.max.x / cellSize).toInt()
        val yStart = floor(aabb.min.y / cellSize).toInt()
        val yEnd = floor(aabb.max.y / cellSize).toInt()

        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells.getOrPut(getCellKeyHash(x, y)) { mutableListOf() }.add(positionalEntity)
            }
        }
    }

    fun queryAll(): Iterable<PositionalEntity> = allEntities

    fun queryInRect(
        rect: CValue<Rectangle>,
    ): Iterable<PositionalEntity> = rect.useContents {
        inflate(cellSize / 2f)

        val xStart = floor(x / cellSize).toInt()
        val xEnd = floor((x + width) / cellSize).toInt()
        val yStart = floor(y / cellSize).toInt()
        val yEnd = floor((y + height) / cellSize).toInt()

        val result = mutableSetOf<PositionalEntity>()
        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells[getCellKeyHash(x, y)]?.let { result.addAll(it) }
            }
        }
        return result
    }

    fun remove(identity: PositionalEntity) {
        allEntities.remove(identity)
        val _ = cells.values.forEach { it.remove(identity) }
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