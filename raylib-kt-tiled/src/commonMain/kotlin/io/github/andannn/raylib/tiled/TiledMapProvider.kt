/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled

import io.github.andannn.raylib.tiled.model.GroupLayer
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.Layer
import io.github.andannn.raylib.tiled.model.TiledMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.json.Json

interface TiledMapProvider {
    fun getMap(): TiledMap

    companion object Factory {
        fun json(file: String): TiledMapProvider = JsonTiledMapProvider(file)
    }
}

private class JsonTiledMapProvider(
    private val file: String,
) : TiledMapProvider {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun getMap(): TiledMap {
        val tmJsonFile = Path(file)
        val tmJsonFileParent = tmJsonFile.parent ?: Path(".")

        val tmJson = SystemFileSystem.source(tmJsonFile).buffered().readString()
        val srcMap = json.decodeFromString<TiledMap>(tmJson)

        val resolvedLayers = srcMap.layers.resolveImagePaths(tmJsonFileParent)

        val resolvedTilesets = srcMap.tilesets.map { tileset ->
            if (tileset.source != null) {
                mergeExternalTileset(base = tmJsonFileParent, tileset)
            } else {
                tileset
            }
        }

        return srcMap.copy(
            layers = resolvedLayers,
            tilesets = resolvedTilesets
        )
    }

    private fun List<Layer>.resolveImagePaths(basePath: Path): List<Layer> {
        return this.map { layer ->
            when (layer) {
                is ImageLayer -> {
                    val imagePath = Path(layer.image)
                    val absolutePath = if (imagePath.isAbsolute) imagePath else Path(basePath, layer.image)
                    layer.copy(image = absolutePath.toString())
                }

                is GroupLayer -> {
                    layer.copy(layers = layer.layers.resolveImagePaths(basePath))
                }

                else -> layer
            }
        }
    }

    private fun mergeExternalTileset(
        base: Path,
        localTileset: Tileset,
    ): Tileset {
        val relativePath = localTileset.source ?: return localTileset
        val tsjPath = Path(base, relativePath)
        val tsXJson = SystemFileSystem.source(tsjPath).buffered().readString()
        val externalTileset = json.decodeFromString<Tileset>(tsXJson)

        val imageStr = localTileset.image ?: externalTileset.image ?: error("Tileset image must be set.")
        val imagePath = Path(imageStr)
        val tsjParentPath = tsjPath.parent ?: Path(".")
        val absoluteImagePath = if (imagePath.isAbsolute) imagePath else Path(tsjParentPath, imageStr)

        return localTileset.copy(
            backgroundColor = localTileset.backgroundColor ?: externalTileset.backgroundColor,
            className = localTileset.className ?: externalTileset.className,
            columns = localTileset.columns ?: externalTileset.columns,
            fillMode = externalTileset.fillMode,
            image = absoluteImagePath.toString(),
            imageHeight = localTileset.imageHeight ?: externalTileset.imageHeight,
            imageWidth = localTileset.imageWidth ?: externalTileset.imageWidth,
            margin = if (localTileset.margin != 0) localTileset.margin else externalTileset.margin,
            name = localTileset.name ?: externalTileset.name,
            objectAlignment = externalTileset.objectAlignment,
            spacing = if (localTileset.spacing != 0) localTileset.spacing else externalTileset.spacing,
            tileCount = localTileset.tileCount ?: externalTileset.tileCount,
            tileHeight = localTileset.tileHeight ?: externalTileset.tileHeight,
            tileRenderSize = externalTileset.tileRenderSize,
            tileWidth = localTileset.tileWidth ?: externalTileset.tileWidth,
            transparentColor = localTileset.transparentColor ?: externalTileset.transparentColor
        )
    }
}
