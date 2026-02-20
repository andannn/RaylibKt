package me.raylib.sample

import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.drawScope
import raylib.core.mainGameLoop
import raylib.core.window

internal fun firstWindow() {
    window(
        title = "raylib [core] example - basic window",
        initialFps = 60,
        width = 800,
        height = 450
    ) {
        mainGameLoop {
            drawScope(Colors.RAYWHITE) {
                drawText("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
            }
        }
    }
}