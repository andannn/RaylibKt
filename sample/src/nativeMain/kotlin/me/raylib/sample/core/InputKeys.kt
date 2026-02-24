package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.MAROON
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.window

internal fun inputKeys() {
    window(
        title = "raylib [core] example - input keys",
        initialFps = 60,
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerComponents {
            component("key") {
                val ballPosition = alloc<Vector2>().apply {
                    x = screenWidth.div(2f)
                    y = screenHeight.div(2f)
                }

                provideHandlers {
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
        }
    }
}