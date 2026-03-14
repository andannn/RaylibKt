package io.github.andannn.raylib.tiled.component

import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.*
import io.github.andannn.raylib.tiled.model.GID
import io.github.andannn.raylib.tiled.model.GroupLayer
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.Layer
import io.github.andannn.raylib.tiled.model.ObjectGroupLayer
import io.github.andannn.raylib.tiled.model.TileLayer
import io.github.andannn.raylib.tiled.model.TiledMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.cinterop.CValue
import kotlinx.io.files.Path

fun ComponentRegistry.tiledComponent(
    key: Any,
    tmJsonFile: String,
    txJsonFileDictionary: String = Path(tmJsonFile).parent.toString()
) = tiledComponent(key, TiledMapProvider(tmJsonFile, txJsonFileDictionary))

fun ComponentRegistry.tiledComponent(
    key: Any,
    tiledMapProvider: TiledMapProvider
) = component(key) {
    val tiledMap = remember { tiledMapProvider.getMap() }
    val tiledSetManager = remember {
        TiledSetTextureManager(this, tiledMap)
    }

    val flattenedLayers = remember {
        tiledMap.flattenTileLayers()
    }

    with(tiledSetManager) {
        flattenedLayers.forEach { layer ->
            drawLayers(layer)
        }
    }

    if (find<WindowContext>().isDebug) {
        onDraw {
            with(tiledMap) {
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
}

context(_: TiledSetTextureManager)
private fun ComponentScope.drawLayers(flatTileLayer: FlatTileLayer) {
    val globalOpacity: Float = flatTileLayer.globalOpacity
    val globalOffsetX: Float = flatTileLayer.globalOffsetX
    val globalOffsetY: Float = flatTileLayer.globalOffsetY
// TODO: handle global property.

    when (val layer = flatTileLayer.layer) {
        is GroupLayer -> error("Never")
        is TileLayer -> drawTileLayer(layer)
        is ImageLayer -> TODO()
        is ObjectGroupLayer -> TODO()
    }
}

context(manager: TiledSetTextureManager)
private fun ComponentScope.drawTileLayer(tileLayer: TileLayer) {
    for (y in 0 until tileLayer.height) {
        for (x in 0 until tileLayer.width) {
            val gid = tileLayer.gidArray[y * tileLayer.width + x]
            if (gid.tileId == 0) continue

            val (src, dst, texture) = manager.get(x, y, gid)

            onDraw {
                drawTexture(texture, src, dst)
            }
        }
    }
}

private fun TiledMap.flattenTileLayers(): List<FlatTileLayer> {
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

private data class FlatTileLayer(
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

private class TiledSetTextureManager(
    val rememberScope: RememberScope,
    val tiledMap: TiledMap,
) {
    private val textureMap: Map<TiledSetKey, TiledSetWithTexture> = buildMap {
        tiledMap.tilesets.forEach {
            put(it.key(), TiledSetWithTexture(it, rememberScope.loadTexture(it.requireImage())))
        }
    }

    fun get(x: Int, y: Int, gID: GID): Triple<CValue<Rectangle>, CValue<Rectangle>, CValue<Texture>> {
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