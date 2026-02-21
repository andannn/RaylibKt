package me.raylib.sample.core

import kotlinx.cinterop.copy
import raylib.core.Colors
import raylib.core.Colors.MAROON
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.drawScope
import raylib.core.gameLoop
import raylib.core.window

internal fun inputKeys() {
    window(
        title = "raylib [core] example - input keys",
        initialFps = 60,
        width = 800,
        height = 450
    ) {
        var ballPosition = Vector2(
            screenWidth.div(2f),
            screenHeight.div(2f)
        )

        gameLoop {
            if (KeyboardKey.KEY_RIGHT.isDown()) ballPosition = ballPosition.copy { x += 2f }
            if (KeyboardKey.KEY_LEFT.isDown()) ballPosition = ballPosition.copy { x -= 2f }
            if (KeyboardKey.KEY_UP.isDown()) ballPosition = ballPosition.copy { y -= 2f }
            if (KeyboardKey.KEY_DOWN.isDown()) ballPosition = ballPosition.copy { y += 2f }

            drawScope(Colors.RAYWHITE) {
                drawText("move the ball with arrow keys", 10, 10, 20, Colors.DARKGRAY);
                drawCircle(ballPosition, 50f, MAROON);
            }
        }
    }
}