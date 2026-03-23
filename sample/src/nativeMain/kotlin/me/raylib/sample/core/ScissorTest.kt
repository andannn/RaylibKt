package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.foundation.Colors
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.Rectangle
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.scissorMode
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue

fun ComponentRegistry.scissorTest() {
    component("key") {
        val scissorArea by remember {
            nativeStateOf { alloc<Rectangle> { x = 0f; y = 0f; width = 300f; height = 300f } }
        }
        var scissorMode by remember {
            mutableStateOf(true)
        }

        update {
            if (KeyboardKey.KEY_S.isPressed()) scissorMode = !scissorMode;

            scissorArea.x = mouseX - scissorArea.width / 2
            scissorArea.y = mouseY - scissorArea.height / 2
        }

        draw {
            scissorMode(scissorArea, enabled = scissorMode) {
                // Draw full screen rectangle and some text
                // NOTE: Only part defined by scissor area will be rendered
                drawRectangle(0, 0, screenWidth, screenHeight, Colors.BROWN)
                drawText("Move the mouse around to reveal this text!", 190, 200, 20f, LIGHTGRAY)
            }

            drawRectangleLines(scissorArea.readValue(), 1f, BLACK)
            drawText("Press S to toggle scissor test", 10, 10, 20f, BLACK)
        }
    }
}
