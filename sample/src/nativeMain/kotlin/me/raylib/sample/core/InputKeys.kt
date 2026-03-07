package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.MAROON
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.component
import raylib.core.getValue
import raylib.core.nativeStateOf
import raylib.core.remember

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

        onUpdate {
            if (KeyboardKey.KEY_RIGHT.isDown()) ballPosition.x += 2f
            if (KeyboardKey.KEY_LEFT.isDown()) ballPosition.x -= 2f
            if (KeyboardKey.KEY_UP.isDown()) ballPosition.y -= 2f
            if (KeyboardKey.KEY_DOWN.isDown()) ballPosition.y += 2f
        }
        onDraw {
            drawText("move the ball with arrow keys", 10, 10, 20, Colors.DARKGRAY)
            drawCircle(ballPosition.readValue(), 50f, MAROON)
        }
    }
}
