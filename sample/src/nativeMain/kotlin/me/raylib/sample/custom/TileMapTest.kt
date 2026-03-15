package me.raylib.sample.custom

import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.requireParentSpatial2D
import io.github.andannn.raylib.components.spatial2DComponent
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.tiled.tiledComponent

fun ComponentRegistry.tileMapTest() = component("tile-mapTest") {
    backGroundColor = BLACK

    tiledComponent("", "resources/tiled/test.tmj") { obj ->
        when (obj.name) {
            "Arrrr" -> {
                val parent2D = requireParentSpatial2D()
                onUpdate {
                    parent2D.transform.position.x += 1f
                }
                onDraw {
                    drawRectangle(Rectangle(0f, 0f, 20f, 20f), BLUE)
                }
            }

            "Rota" -> {
                spatial2DComponent("item", Vector2(20f, 20f)) { transform ->
                    onUpdate {
                        transform.transform.position.x += 1f
                    }
                    onDraw {
                        drawRectangle(Rectangle(0f, 0f, 20f, 20f), RED)
                    }
                }
            }
        }
    }
}