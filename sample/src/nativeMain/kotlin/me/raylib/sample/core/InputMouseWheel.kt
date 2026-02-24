package me.raylib.sample.core

import raylib.core.Colors
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.window

fun inputMouseWheel() {
    window(
        title = "raylib [core] example - input mouse wheel",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerComponents {
            component("key") {
                var boxPositionY = screenHeight.div(2f) - 40
                val scrollSpeed = 4f
                provideHandlers {
                    onUpdate {
                        boxPositionY -= mouseWheelMove * scrollSpeed
                    }

                    onDraw {
                        drawRectangle(screenWidth / 2 - 40, boxPositionY.toInt(), 80, 80, MAROON)
                        drawText("Use mouse wheel to move the cube up and down!", 10, 10, 20, GRAY)
                        drawText("Box position Y: $boxPositionY", 10, 40, 20, LIGHTGRAY)
                    }
                }
            }
        }
    }
}