package me.raylib.sample.core

import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.runtime.setValue

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
            drawText("Use mouse wheel to move the cube up and down!", 10, 10, 20f, GRAY)
            drawText("Box position Y: $boxPositionY", 10, 40, 20f, LIGHTGRAY)
        }
    }
}
