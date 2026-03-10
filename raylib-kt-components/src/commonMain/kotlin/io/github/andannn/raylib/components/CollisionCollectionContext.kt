package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.core.Context
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlin.math.floor

class CollisionCollectionContext(
    private val cellSize: Int
) : Context {
    internal val cells = mutableMapOf<Long, MutableList<Spatial2DBoxState>>()

    fun register(state: Spatial2DBoxState) {
        val aabb = state.aabb
        val xStart = floor(aabb.min.x / cellSize).toInt()
        val xEnd = floor(aabb.max.x / cellSize).toInt()
        val yStart = floor(aabb.min.y / cellSize).toInt()
        val yEnd = floor(aabb.max.y / cellSize).toInt()

        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                cells.getOrPut(getCellKeyHash(x, y)) { mutableListOf() }.add(state)
            }
        }
        println("called ${cells}")
    }

    fun queryIn(rect: CValue<Rectangle>): Set<Spatial2DBoxState> = rect.useContents {
        val rangeRect = this
        val xStart = floor(rangeRect.x / cellSize).toInt()
        val xEnd = floor((rangeRect.x + rangeRect.width) / cellSize).toInt()
        val yStart = floor(rangeRect.y / cellSize).toInt()
        val yEnd = floor((rangeRect.y + rangeRect.height) / cellSize).toInt()

        val result = mutableSetOf<Spatial2DBoxState>()
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