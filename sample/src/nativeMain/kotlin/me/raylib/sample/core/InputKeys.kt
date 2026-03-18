package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.base.Colors
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember

internal fun ComponentRegistry.inputKeys() {
    component("key") {
        val ballPosition by remember {
            nativeStateOf {
                alloc<Vector2> {
                    x = screenWidth.div(2f)
                    y = screenHeight.div(2f)
                }
            }
        }

        update {
            if (KeyboardKey.KEY_RIGHT.isDown()) ballPosition.x += 2f
            if (KeyboardKey.KEY_LEFT.isDown()) ballPosition.x -= 2f
            if (KeyboardKey.KEY_UP.isDown()) ballPosition.y -= 2f
            if (KeyboardKey.KEY_DOWN.isDown()) ballPosition.y += 2f
        }
        draw {
            drawText("move the ball with arrow keys", 10, 10, 20, Colors.DARKGRAY)
            drawCircle(ballPosition.readValue(), 50f, MAROON)
        }
    }
}
