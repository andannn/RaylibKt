package io.github.andannn.raylib.tiled.render

import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Texture
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.AssetManager
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.tiled.model.GID
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.TileLayer
import io.github.andannn.raylib.tiled.model.TileMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.cinterop.CValue

@PublishedApi
internal fun ComponentScope.drawImageLayer(layer: ImageLayer, tint: CValue<Color>) {
// TODO: handle image transparent color.
// Raylib C API: RLAPI void ImageColorReplace(Image *image, Color color, Color replace);

// TODO: handle repeatx, repeaty
    val imageTexture = remember {
        find<AssetManager>().getTexture(layer.image)
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

                val (src, dst, texture) = manager.getTile(x, y, gid)

                drawTexture(texture = texture, source = src, dest = dst, tint = tint)
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
    fun getTile(x: Int, y: Int, gID: GID): Triple<CValue<Rectangle>, CValue<Rectangle>, CValue<Texture>>
}

@PublishedApi
internal fun TiledSetManager(
    assetManager: AssetManager,
    tileMap: TileMap,
): TiledSetManager = TiledSetTextureManager(assetManager, tileMap)

private class TiledSetTextureManager(
    val assetManager: AssetManager,
    val tileMap: TileMap,
) : TiledSetManager {
    private val textureMap: Map<TiledSetKey, TiledSetWithTexture> = buildMap {
        tileMap.tilesets.forEach {
            put(it.key(), TiledSetWithTexture(it, assetManager.getTexture(it.requireImage())))
        }
    }

    override val tileHeight = tileMap.tileHeight
    override val tileWidth = tileMap.tileWidth

    override fun getTile(x: Int, y: Int, gID: GID): Triple<CValue<Rectangle>, CValue<Rectangle>, CValue<Texture>> {
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
            x * tileMap.tileWidth.toFloat(),
            y * tileMap.tileHeight.toFloat(),
            tileMap.tileWidth.toFloat(),
            tileMap.tileHeight.toFloat()
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
