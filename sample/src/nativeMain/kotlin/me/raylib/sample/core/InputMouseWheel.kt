package me.raylib.sample.core

import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.inputMouseWheel() {
    component("key") {
        var boxPositionY by remember {
            mutableStateOf(screenHeight.div(2f) - 40)
        }
        val scrollSpeed = 4f
        update {
            boxPositionY -= mouseWheelMove * scrollSpeed
        }

        draw {
            drawRectangle(screenWidth / 2 - 40, boxPositionY.toInt(), 80, 80, MAROON)
            drawText("Use mouse wheel to move the cube up and down!", 10, 10, 20, GRAY)
            drawText("Box position Y: $boxPositionY", 10, 40, 20, LIGHTGRAY)
        }
    }
}
