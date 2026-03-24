package me.raylib.sample.custom

import io.github.andannn.raylib.assets.fileResourceResolver
import io.github.andannn.raylib.assets.rresResourceResolver
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.BLUE
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.components.requireParentSpatial2D
import io.github.andannn.raylib.components.spatial2DComponent
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.windowContext
import io.github.andannn.raylib.tiled.tiledComponent

fun ComponentRegistry.tileMapTest() = component("tile-mapTest") {
    windowContext.backGroundColor = BLACK

    tiledComponent("", "tiled/test.tmj", rresResourceResolver) { obj ->
        when (obj.name) {
            "Arrrr" -> {
                val parent2D = requireParentSpatial2D()
                draw {
                    drawRectangle(Rectangle(0f, 0f, 20f, 20f), BLUE)
                }
            }

            "Rota" -> {
                spatial2DComponent("item", Vector2(20f, 20f)) { transform ->
                    draw {
                        drawRectangle(Rectangle(0f, 0f, 20f, 20f), RED)
                    }
                }
            }

            "TileObj" -> {
                println("obj property ${obj.properties}")
            }
        }
    }
}