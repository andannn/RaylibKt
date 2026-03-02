package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.MAROON
import raylib.core.ComponentFactory
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.stateOf

internal fun ComponentFactory.inputKeys() {
    component("key") {
        val ballPosition by stateOf {
            alloc<Vector2> {
                x = screenWidth.div(2f)
                y = screenHeight.div(2f)
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
