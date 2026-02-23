package me.raylib.sample.core

import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.window

internal fun firstWindow() {
    window(
        title = "raylib [core] example - basic window",
        initialFps = 60,
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerGameComponents {
            component("key") {
                provideHandlers {
                    onDraw {
                        drawText("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
                    }
                }
            }
        }
    }
}