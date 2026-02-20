package me.raylib.sample.core

import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.RAYWHITE
import raylib.core.drawScope
import raylib.core.gameLoop
import raylib.core.window

fun inputMouseWheel() {
    window(
        title = "raylib [core] example - input mouse wheel",
        width = 800,
        height = 450,
    ) {
        var boxPositionY = windowHeight.div(2f) - 40
        val scrollSpeed = 4f
        gameLoop {
            boxPositionY -= wheelMove * scrollSpeed

            drawScope(RAYWHITE) {
                drawRectangle(windowWidth / 2 - 40, boxPositionY.toInt(), 80, 80, MAROON)

                drawText("Use mouse wheel to move the cube up and down!", 10, 10, 20, GRAY)
                drawText("Box position Y: $boxPositionY", 10, 40, 20, LIGHTGRAY)
            }
        }
    }
}