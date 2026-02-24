package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.RAYWHITE
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.scissorMode
import raylib.core.window

fun scissorTest() {
    window(
        title = "raylib [core] example - scissor test",
        width = 800,
        height = 450,
        initialBackGroundColor = RAYWHITE
    ) {
        registerComponents {
            component("key") {
                val scissorArea = alloc<Rectangle> { x = 0f; y = 0f; width = 300f; height = 300f }
                var scissorMode = true

                provideHandlers {
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
        }
    }
}