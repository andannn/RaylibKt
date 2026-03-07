package me.raylib.sample.core

import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.ComponentRegistry
import raylib.core.component
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.remember
import raylib.core.setValue

fun ComponentRegistry.inputMouseWheel() {
    component("key") {
        var boxPositionY by remember {
            mutableStateOf(screenHeight.div(2f) - 40)
        }
        val scrollSpeed = 4f
        onUpdate {
            boxPositionY -= mouseWheelMove * scrollSpeed
        }

        onDraw {
            drawRectangle(screenWidth / 2 - 40, boxPositionY.toInt(), 80, 80, MAROON)
            drawText("Use mouse wheel to move the cube up and down!", 10, 10, 20, GRAY)
            drawText("Box position Y: $boxPositionY", 10, 40, 20, LIGHTGRAY)
        }
    }
}
