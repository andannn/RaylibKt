package me.raylib.sample.shape

import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.GREEN
import raylib.core.Colors.RED
import raylib.core.window
import raylib.interop.Fade

fun easingBall() {
    window(
        title = "raylib [shapes] example - easings ball",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("A") {
                val ballPositionX = -100
                val ballRadius = 20
                val ballAlpha = 0.0f

                val state = 0
                val framesCounter = 0

                provideHandlers {
                    onUpdate {

                    }
                    onDraw {
                        if (state >= 2) drawRectangle(0, 0, screenWidth, screenHeight, GREEN)
                        drawCircle(ballPositionX, 200, ballRadius.toFloat(), Fade(RED, 1.0f - ballAlpha))
                        if (state == 3) drawText("PRESS [ENTER] TO PLAY AGAIN!", 240, 200, 20, BLACK)
                    }
                }
            }
        }
    }
}

