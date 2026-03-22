/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.isCollisionWith
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.runtime.Context
import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.foundation.GameContext
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.doOnce
import io.github.andannn.raylib.runtime.findOrNull
import io.github.andannn.raylib.runtime.provide
import io.github.andannn.raylib.runtime.remember
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
    position: Spatial2D,
    extra: Any? = null
) {
    doOnce {
        val contextOrNull = findOrNull<WorldGrid2DContext>()
        val entity = Spatial2DModel(entity, position, extra)
        contextOrNull?.register(entity)

        disposeOnClose {
            contextOrNull?.remove(identity = entity)
        }
    }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Spatial2D.queryNearby(crossinline block: (Spatial2DModel) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())?.forEach(block)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Spatial2D.queryNearby(crossinline block: (T, Spatial2D, Any?) -> Unit) {
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())
        ?.filter { (entity, _, _) -> entity is T }
        ?.forEach {
            block(it.entity as T, it.position, it.extra)
        }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Spatial2D.queryNearbyUntil(crossinline block: (Spatial2DModel) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())?.any { block(it) }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Spatial2D.queryNearbyUntil(crossinline block: (T, Spatial2D, Any?) -> Boolean) {
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(this.aabb.toGlobalRect())
        ?.filter { (entity, _, _) -> entity is T }
        ?.any {
            block(it.entity as T, it.position, it.extra)
        }
}

inline fun <reified T : Entity> ContextProvider.allEntities(): List<T> {
    return findOrNull<WorldGrid2DContext>()
        ?.queryAll()
        ?.mapNotNull { it.entity as? T }
        ?: emptyList()
}

inline fun <reified T : Entity> ContextProvider.firstOrNull(): Pair<T, Spatial2D>? {
    val model = findOrNull<WorldGrid2DContext>()
        ?.queryAll()
        ?.firstOrNull { it.entity is T } ?: return null

    return Pair(model.entity as T, model.position)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Spatial2D.queryAABBCollision(crossinline block: (Spatial2DModel) -> Unit) {
    val targetRect = this.aabb.toGlobalRect()
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(targetRect)
        ?.filter {
            it.position.toGlobalRect().isCollisionWith(targetRect)
        }
        ?.forEach(block)
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Spatial2D.queryAABBCollision(crossinline block: (T, Spatial2D, Any?) -> Unit) {
    val targetRect = this.aabb.toGlobalRect()
    contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(targetRect)
        ?.filter { (entity, position, _) ->
            entity is T && position.toGlobalRect().isCollisionWith(targetRect)
        }
        ?.forEach {
            block(it.entity as T, it.position, it.extra)
        }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun Spatial2D.queryAABBCollisionUntil(crossinline block: (Spatial2DModel) -> Boolean) {
    val targetRect = this.aabb.toGlobalRect()
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(targetRect)
        ?.any {
            it.position.toGlobalRect().isCollisionWith(targetRect) && block(it)
        }
}

context(_: GameContext, contextProvider: ContextProvider)
inline fun <reified T : Entity> Spatial2D.queryAABBCollisionUntil(crossinline block: (T, Spatial2D, Any?) -> Boolean) {
    val targetRect = this.aabb.toGlobalRect()
    val _ = contextProvider.findOrNull<WorldGrid2DContext>()?.queryInRect(targetRect)
        ?.any {
            it.entity is T && it.position.toGlobalRect().isCollisionWith(targetRect) &&
                    block(it.entity, it.position, it.extra)
        }
}

data class Spatial2DModel(
    val entity: Entity,
    val position: Spatial2D,
    val extra: Any? = null
)

/**
 * For 2D spatial partitioning.
 * Maps world coordinates to discrete grid cells to optimize broad-phase collision detection.
 */
class WorldGrid2DContext(
    private val cellSize: Int,
) : Context {
    private val cells = mutableMapOf<Long, MutableList<Spatial2DModel>>()
    private val allEntities = mutableListOf<Spatial2DModel>()

    fun register(spatial2DModel: Spatial2DModel) {
        allEntities.add(spatial2DModel)

        val aabb = spatial2DModel.position.aabb
        val xStart = floor(aabb.min.x / cellSize).toInt()
        val xEnd = floor(aabb.max.x / cellSize).toInt()
        val yStart = floor(aabb.min.y / cellSize).toInt()
        val yEnd = floor(aabb.max.y / cellSize).toInt()

        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells.getOrPut(getCellKeyHash(x, y)) { mutableListOf() }.add(spatial2DModel)
            }
        }
    }

    fun queryAll(): Iterable<Spatial2DModel> = allEntities

    fun queryInRect(
        rect: CValue<Rectangle>,
    ): Iterable<Spatial2DModel> = rect.useContents {
        inflate(cellSize / 2f)

        val xStart = floor(x / cellSize).toInt()
        val xEnd = floor((x + width) / cellSize).toInt()
        val yStart = floor(y / cellSize).toInt()
        val yEnd = floor((y + height) / cellSize).toInt()

        val result = mutableSetOf<Spatial2DModel>()
        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells[getCellKeyHash(x, y)]?.let { result.addAll(it) }
            }
        }
        return result
    }

    fun remove(identity: Spatial2DModel) {
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