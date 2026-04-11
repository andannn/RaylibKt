/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.render

import io.github.andannn.raylib.assets.ResourceResolver
import io.github.andannn.raylib.foundation.Color
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.foundation.DrawContext
import io.github.andannn.raylib.foundation.Texture
import io.github.andannn.raylib.foundation.WindowContext
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.find
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.tiled.model.GID
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.TileLayer
import io.github.andannn.raylib.tiled.model.TileMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.cinterop.CValue

@PublishedApi
context(resourceResolver: ResourceResolver)
internal fun ComponentScope.drawImageLayer(layer: ImageLayer, tint: CValue<Color>) {
// TODO: handle image transparent color.
// Raylib C API: RLAPI void ImageColorReplace(Image *image, Color color, Color replace);

// TODO: handle repeatx, repeaty
    val imageTexture = remember {
        resourceResolver.resolveImageTexture(layer.image)
    }

    draw {
        drawTexture(imageTexture, 0, 0, tint = tint)
    }
}


@PublishedApi
context(manager: TiledSetManager)
internal fun ComponentScope.drawTileLayer(tileLayer: TileLayer, tint: CValue<Color>) {
    draw {
        for (y in 0 until tileLayer.height) {
            for (x in 0 until tileLayer.width) {
                val gid = tileLayer.gidArray[y * tileLayer.width + x]
                if (gid.tileId == 0) continue

                drawTile(x.toFloat(), y.toFloat(), gid, getTileSetByGID(gid),tint)
            }
        }

        val isDebug = remember { find<WindowContext>().isDebug }
        if (isDebug) {
            val height = tileLayer.height
            val width = tileLayer.width
            val tileHeight = manager.tileHeight
            val tileWidth = manager.tileWidth
            val totalHeight = height * tileHeight.toFloat()
            for (i in 0..width) {
                drawLine(
                    Vector2(i * tileWidth.toFloat(), 0f),
                    Vector2(i * tileWidth.toFloat(), totalHeight),
                    color = LIGHTGRAY
                )
            }

            val totalWidth = width * tileWidth.toFloat()
            for (j in 0..height) {
                drawLine(
                    Vector2(0f, j * tileHeight.toFloat()),
                    Vector2(totalWidth, j * tileHeight.toFloat()),
                    color = LIGHTGRAY
                )
            }
        }

    }
}

private fun Tileset.key(): TiledSetKey {
    return TiledSetKey(
        requireFirstGid()..<(requireFirstGid() + requireTileCount())
    )
}

@PublishedApi
internal class TiledSetKey(
    val localIdRange: IntRange,
)


@PublishedApi
internal interface TiledSetManager {
    val tileHeight: Int
    val tileWidth: Int
    val textureMap: Map<TiledSetKey, TiledSetWithTexture>
}

@PublishedApi
context(tileSetManager: TiledSetManager)
internal fun getTileSetByGID(gid: GID): TiledSetWithTexture {
    val tileId = gid.tileId
    val key = tileSetManager.textureMap.keys.firstOrNull { tileId in it.localIdRange }
        ?: throw IllegalStateException("request gid: $gid is not registered in TiledSetTextureManager")
    return tileSetManager.textureMap[key]!!
}

@PublishedApi
internal fun DrawContext.drawTile(
    x: Float,
    y: Float,
    gID: GID,
    tileSet: TiledSetWithTexture,
    tint: CValue<Color>,
) {
    val flipHorizontal = gID.flipHorizontal
    val flipVertical = gID.flipVertical
    val flipDiagonal = gID.flipDiagonal
// TODO: handle flipDiagonal
    val tileId = gID.tileId
    val (tileSet, texture) = tileSet

    val localId = tileId - tileSet.requireFirstGid()
    val srcRect = calculateSourceRect(localId, tileSet, flipHorizontal, flipVertical)

    val tileWidth = tileSet.requireTileWidth().toFloat()
    val tileHeight = tileSet.requireTileHeight().toFloat()
    val destRect = Rectangle(
        x * tileWidth,
        y * tileHeight,
        tileWidth,
        tileHeight
    )
    drawTexture(texture = texture, source = srcRect, dest = destRect, tint = tint)
}

@PublishedApi
internal fun TiledSetManager(
    resourceResolver: ResourceResolver,
    tileMap: TileMap,
): TiledSetManager = TiledSetTextureManager(resourceResolver, tileMap)

private class TiledSetTextureManager(
    val resourceResolver: ResourceResolver,
    val tileMap: TileMap,
) : TiledSetManager {
    override val textureMap: Map<TiledSetKey, TiledSetWithTexture> = buildMap {
        tileMap.tilesets.forEach {
            put(it.key(), TiledSetWithTexture(it, resourceResolver.resolveImageTexture(it.requireImage())))
        }
    }

    override val tileHeight = tileMap.tileHeight
    override val tileWidth = tileMap.tileWidth
}

private fun calculateSourceRect(
    tileId: Int,
    tileset: Tileset,
    flipHorizontal: Boolean,
    flipVertical: Boolean,
): CValue<Rectangle> {
    val tileWidth = tileset.requireTileWidth().toFloat()
    val tileHeight = tileset.requireTileHeight().toFloat()
    val columns = tileset.requireColumns()

    val col = tileId % columns
    val row = tileId / columns

    val x = col * tileWidth
    val y = row * tileHeight

    val width = if (flipHorizontal) -tileWidth else tileWidth
    val height = if (flipVertical) -tileHeight else tileHeight

    return Rectangle(
        x = x,
        y = y,
        width = width,
        height = height
    )
}

@PublishedApi
internal data class TiledSetWithTexture(
    val tileset: Tileset,
    val texture: CValue<Texture>
)
