package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.base.Colors
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.base.scissorMode
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.scissorTest() {
    component("key") {
        val scissorArea by remember {
            nativeStateOf { alloc<Rectangle> { x = 0f; y = 0f; width = 300f; height = 300f } }
        }
        var scissorMode by remember {
            mutableStateOf(true)
        }

        onUpdate {
            if (KeyboardKey.KEY_S.isPressed()) scissorMode = !scissorMode;

            scissorArea.x = mouseX - scissorArea.width / 2
            scissorArea.y = mouseY - scissorArea.height / 2
        }

        onDraw {
            scissorMode(scissorArea, enabled = scissorMode) {
                // Draw full screen rectangle and some text
                // NOTE: Only part defined by scissor area will be rendered
                drawRectangle(0, 0, screenWidth, screenHeight, Colors.BROWN)
                drawText("Move the mouse around to reveal this text!", 190, 200, 20, LIGHTGRAY)
            }

            drawRectangleLines(scissorArea.readValue(), 1f, BLACK)
            drawText("Press S to toggle scissor test", 10, 10, 20, BLACK)
        }
    }
}
