package me.raylib.sample.custom

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
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.windowContext
import io.github.andannn.raylib.tiled.TiledMapProvider.Factory.rres
import io.github.andannn.raylib.tiled.tiledComponent

fun ComponentRegistry.tileMapTest() = component("tile-mapTest") {
    windowContext.backGroundColor = BLACK

    val provider = remember {
        rres("tiled/test.tmj")
    }
    tiledComponent("", provider) { obj ->
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
        }
    }
}