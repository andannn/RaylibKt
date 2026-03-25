/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled

import io.github.andannn.raylib.assets.ResourceResolver
import io.github.andannn.raylib.tiled.model.GroupLayer
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.Layer
import io.github.andannn.raylib.tiled.model.Object
import io.github.andannn.raylib.tiled.model.ObjectGroupLayer
import io.github.andannn.raylib.tiled.model.Template
import io.github.andannn.raylib.tiled.model.TemplateObject
import io.github.andannn.raylib.tiled.model.TileMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json

internal interface TiledMapProvider {
    val resourceResolver: ResourceResolver

    fun getMap(): TileMap
}

@PublishedApi
internal class JsonTiledMapProvider(
    override val resourceResolver: ResourceResolver,
    private val file: String,
) : TiledMapProvider {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun getMap(): TileMap {
        val tmJsonFile = Path(file)
        val tmJsonFileParent = tmJsonFile.parent ?: Path(".")

        val tmJson = resourceResolver.resolveText(file)
        val srcMap = json.decodeFromString<TileMap>(tmJson)

        val resolvedLayers = srcMap.layers.resolvePaths(tmJsonFileParent)

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

    private fun List<Layer>.resolvePaths(basePath: Path): List<Layer> {
        return this.map { layer ->
            when (layer) {
                is ImageLayer -> {
                    val imagePath = Path(layer.image)
                    val absolutePath = if (imagePath.isAbsolute) imagePath else Path(basePath, layer.image)
                    layer.copy(image = absolutePath.toString())
                }

                is ObjectGroupLayer -> {
                    val newObjects = layer.objects.map { obj ->
                        when (obj) {
                            is TemplateObject -> {
                                resolveTemplateObj(basePath, obj)
                            }

                            else -> obj
                        }
                    }
                    layer.copy(objects = newObjects)
                }

                is GroupLayer -> {
                    layer.copy(layers = layer.layers.resolvePaths(basePath))
                }

                else -> layer
            }
        }
    }

    private fun resolveTemplateObj(basePath: Path, obj: TemplateObject): Object {
        val templatePath = Path(obj.template)
        val absolutePath = if (templatePath.isAbsolute) templatePath else Path(basePath, obj.template)

        val templateJson = resourceResolver.resolveText(absolutePath.toString())
        val template = json.decodeFromString<Template>(templateJson)
        return template.obj.copyWith(obj.x, obj.y, obj.id)
    }

    private fun mergeExternalTileset(
        base: Path,
        localTileset: Tileset,
    ): Tileset {
        val relativePath = localTileset.source ?: return localTileset
        val tsjPath = Path(base, relativePath)
        val tsXJson = resourceResolver.resolveText(tsjPath.toString())
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
