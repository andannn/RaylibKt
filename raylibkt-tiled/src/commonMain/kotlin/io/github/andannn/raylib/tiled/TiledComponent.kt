/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled

import io.github.andannn.raylib.assets.ResourceResolver
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.components.Transform2DAlloc
import io.github.andannn.raylib.components.transform2DComponent
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.foundation.WindowContext
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.find
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.tiled.model.GroupLayer
import io.github.andannn.raylib.tiled.model.ImageLayer
import io.github.andannn.raylib.tiled.model.Layer
import io.github.andannn.raylib.tiled.model.Object
import io.github.andannn.raylib.tiled.model.ObjectGroupLayer
import io.github.andannn.raylib.tiled.model.TileLayer
import io.github.andannn.raylib.tiled.model.TileMap
import io.github.andannn.raylib.tiled.render.TiledSetManager
import io.github.andannn.raylib.tiled.render.bindObjects
import io.github.andannn.raylib.tiled.render.drawImageLayer
import io.github.andannn.raylib.tiled.render.drawTileLayer
import io.github.andannn.raylib.tiled.util.blend
import io.github.andannn.raylib.tiled.util.multiplyAlpha

inline fun ComponentRegistry.tiledComponent(
    key: Any,
    tmjFile: String,
    resourceResolver: ResourceResolver,
    crossinline onBindObject: ComponentScope.(Object) -> Unit = {}
): TileMap = component(key) {
    val tiledMapProvider = remember { JsonTiledMapProvider(resourceResolver, tmjFile) }
    val tiledMap = remember { tiledMapProvider.getMap() }
    val tiledSetManager = remember {
        TiledSetManager(tiledMapProvider.resourceResolver, tiledMap)
    }

    val flattenedLayers = remember {
        tiledMap.flattenTileLayers()
    }

    context(tiledSetManager, tiledMapProvider.resourceResolver) {
        flattenedLayers.forEach { flatTileLayer ->
            val globalOpacity: Float = flatTileLayer.globalOpacity
            val globalOffsetX: Float = flatTileLayer.globalOffsetX
            val globalOffsetY: Float = flatTileLayer.globalOffsetY
// TODO: handle Parallax factor.

            val localOffsetX: Float = globalOffsetX
            val localOffsetY: Float = globalOffsetY
            val tintColor = flatTileLayer.layer.raylibTintColor.multiplyAlpha(globalOpacity)
            val transform = remember {
                Transform2DAlloc(position = Vector2(localOffsetX, localOffsetY))
            }

            transform2DComponent(flatTileLayer.layer.id, transform) {
                flatTileLayer.layer.mode.blend {
                    when (val layer = flatTileLayer.layer) {
                        is TileLayer -> drawTileLayer(layer, tintColor)
                        is ImageLayer -> drawImageLayer(layer, tintColor)
                        is ObjectGroupLayer -> bindObjects(layer.objects, onBindObject)
                        is GroupLayer -> error("Never")
                    }
                }
            }
        }
    }

    val isDebug = remember { find<WindowContext>().isDebug }
    if (isDebug) {
        draw {
            val totalHeight = tiledMap.height * tiledMap.tileHeight.toFloat()
            val totalWidth = tiledMap.width * tiledMap.tileWidth.toFloat()

            drawRectangleLines(Rectangle(0f, 0f, totalWidth, totalHeight), 2f, LIGHTGRAY)
        }
    }

    tiledMap
}

@PublishedApi
internal data class FlatTileLayer(
    val layer: Layer,
    val globalOpacity: Float,
    val globalOffsetX: Float,
    val globalOffsetY: Float
)

@PublishedApi
internal fun TileMap.flattenTileLayers(): List<FlatTileLayer> {
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
