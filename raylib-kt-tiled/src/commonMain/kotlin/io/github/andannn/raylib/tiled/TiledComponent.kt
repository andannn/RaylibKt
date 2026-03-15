/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled

import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.Transform2DAlloc
import io.github.andannn.raylib.components.transform2DComponent
import io.github.andannn.raylib.core.*
import io.github.andannn.raylib.tiled.model.GID
import io.github.andannn.raylib.tiled.model.GroupLayer
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.Layer
import io.github.andannn.raylib.tiled.model.ObjectGroupLayer
import io.github.andannn.raylib.tiled.model.TileLayer
import io.github.andannn.raylib.tiled.model.TiledMap
import io.github.andannn.raylib.tiled.model.TiledObject
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.cinterop.CValue
import kotlinx.io.files.Path

inline fun ComponentRegistry.tiledComponent(
    key: Any,
    tmJsonFile: String,
    txJsonFileDictionary: String = Path(tmJsonFile).parent.toString(),
    crossinline onBindObject: ComponentScope.(TiledObject) -> Unit = {}
) = tiledComponent(key, TiledMapProvider(tmJsonFile, txJsonFileDictionary), onBindObject)

inline fun ComponentRegistry.tiledComponent(
    key: Any,
    tiledMapProvider: TiledMapProvider,
    crossinline onBindObject: ComponentScope.(TiledObject) -> Unit = {}
) = component(key) {
    val tiledMap = remember { tiledMapProvider.getMap() }
    val tiledSetManager = remember {
        TiledSetManager(this, tiledMap)
    }

    val flattenedLayers = remember {
        tiledMap.flattenTileLayers()
    }

    context(tiledSetManager) {
        flattenedLayers.forEach { flatTileLayer ->
            val globalOpacity: Float = flatTileLayer.globalOpacity
            val globalOffsetX: Float = flatTileLayer.globalOffsetX
            val globalOffsetY: Float = flatTileLayer.globalOffsetY
// TODO: handle Parallax factor.

            val localOffsetX: Float = globalOffsetX
            val localOffsetY: Float = globalOffsetY

            val transform = remember {
                Transform2DAlloc(position = Vector2(localOffsetX, localOffsetY))
            }

            transform2DComponent(flatTileLayer.layer.id, transform) { transform ->
                flatTileLayer.layer.mode.blend {
                    when (val layer = flatTileLayer.layer) {
                        is TileLayer -> drawTileLayer(layer)
                        is ObjectGroupLayer -> bindObjects(layer.objects, onBindObject)
                        is ImageLayer -> TODO()
                        is GroupLayer -> error("Never")
                    }
                }
            }
        }
    }

    onDraw {
        if (isDebug) {
            val totalHeight = tiledMap.height * tiledMap.tileHeight.toFloat()
            val totalWidth = tiledMap.width * tiledMap.tileWidth.toFloat()

            drawRectangleLines(Rectangle(0f, 0f, totalWidth, totalHeight), 2f, LIGHTGRAY)
        }
    }
}


@PublishedApi
context(manager: TiledSetManager)
internal fun ComponentScope.drawTileLayer(tileLayer: TileLayer) {
    onDraw {
        for (y in 0 until tileLayer.height) {
            for (x in 0 until tileLayer.width) {
                val gid = tileLayer.gidArray[y * tileLayer.width + x]
                if (gid.tileId == 0) continue

                val (src, dst, texture) = manager.get(x, y, gid)

                drawTexture(texture, src, dst)
            }
        }

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

@PublishedApi
internal fun TiledMap.flattenTileLayers(): List<FlatTileLayer> {
    return buildList {
        fun collect(
            layers: List<Layer>,
            parentOpacity: Float = 1.0f,
            parentOffsetX: Float = 0.0f,
            parentOffsetY: Float = 0.0f
        ) {
            for (layer in layers) {
                if (!layer.visible) continue

                val currentOpacity = parentOpacity * layer.opacity.toFloat()
                val currentOffsetX = parentOffsetX + layer.offsetX.toFloat()
                val currentOffsetY = parentOffsetY + layer.offsetY.toFloat()

                when (layer) {
                    is ImageLayer,
                    is ObjectGroupLayer,
                    is TileLayer -> {
                        add(FlatTileLayer(layer, currentOpacity, currentOffsetX, currentOffsetY))
                    }

                    is GroupLayer -> {
                        collect(layer.layers, currentOpacity, currentOffsetX, currentOffsetY)
                    }
                }
            }
        }
        collect(this@flattenTileLayers.layers)
    }
}

@PublishedApi
internal data class FlatTileLayer(
    val layer: Layer,
    val globalOpacity: Float,
    val globalOffsetX: Float,
    val globalOffsetY: Float
)

private fun Tileset.key(): TiledSetKey {
    return TiledSetKey(
        requireFirstGid()..<(requireFirstGid() + requireTileCount())
    )
}

private class TiledSetKey(
    val localIdRange: IntRange,
)


@PublishedApi
internal interface TiledSetManager {
    val tileHeight: Int
    val tileWidth: Int
    fun get(x: Int, y: Int, gID: GID): Triple<CValue<Rectangle>, CValue<Rectangle>, CValue<Texture>>
}

@PublishedApi
internal fun TiledSetManager(
    rememberScope: RememberScope,
    tiledMap: TiledMap,
): TiledSetManager = TiledSetTextureManager(rememberScope, tiledMap)

private class TiledSetTextureManager(
    val rememberScope: RememberScope,
    val tiledMap: TiledMap,
) : TiledSetManager {
    private val textureMap: Map<TiledSetKey, TiledSetWithTexture> = buildMap {
        tiledMap.tilesets.forEach {
            put(it.key(), TiledSetWithTexture(it, rememberScope.loadTexture(it.requireImage())))
        }
    }

    override val tileHeight = tiledMap.tileHeight
    override val tileWidth = tiledMap.tileWidth

    override fun get(x: Int, y: Int, gID: GID): Triple<CValue<Rectangle>, CValue<Rectangle>, CValue<Texture>> {
        val flipHorizontal = gID.flipHorizontal
        val flipVertical = gID.flipVertical
        val flipDiagonal = gID.flipDiagonal
// TODO: handle flip

        val tileId = gID.tileId

        val key = textureMap.keys.firstOrNull { tileId in it.localIdRange }
            ?: throw IllegalStateException("request gID: ${gID} is not registered in TiledSetTextureManager")
        val (tileSet, texture) = textureMap[key]!!

        val localId = tileId - tileSet.requireFirstGid()
        val srcRect = calculateSourceRect(localId, tileSet)

        val destRect = Rectangle(
            x * tiledMap.tileWidth.toFloat(),
            y * tiledMap.tileHeight.toFloat(),
            tiledMap.tileWidth.toFloat(),
            tiledMap.tileHeight.toFloat()
        )

        return Triple(srcRect, destRect, texture)
    }
}

private fun calculateSourceRect(tileId: Int, tileset: Tileset): CValue<Rectangle> {
    val tileWidth = tileset.requireTileWidth().toFloat()
    val tileHeight = tileset.requireTileHeight().toFloat()
    val columns = tileset.requireColumns()

    return Rectangle(
        (tileId % columns) * tileWidth,
        (tileId / columns).toFloat() * tileHeight,
        tileWidth,
        tileHeight
    )
}

private data class TiledSetWithTexture(
    val tileset: Tileset,
    val texture: CValue<Texture>
)