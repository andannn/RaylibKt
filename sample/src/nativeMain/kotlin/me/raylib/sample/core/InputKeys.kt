package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.foundation.Colors
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth

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
            drawText("move the ball with arrow keys", 10, 10, 20f, Colors.DARKGRAY)
            drawCircle(ballPosition.readValue(), 50f, MAROON)
        }
    }
}
