package me.raylib.sample.shape

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.DARKGREEN
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.RED
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.window

fun bouncingBall() {
    window(
        title = "raylib [shapes] example - bouncing ball",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        registerGameComponents {
            component("key") {
                val ballPosition = alloc<Vector2> { x = screenWidth.div(2f); y = screenHeight.div(2f) }
                val ballSpeed = alloc<Vector2> { x = 5f; y = 4f }
                var useGravity = true
                var pause = false

                val ballRadius = 20
                val gravity = 0.2f
                var framesCounter = 0

                provideHandlers {
                    onUpdate {
                        if (KeyboardKey.KEY_G.isPressed()) useGravity = !useGravity;
                        if (KeyboardKey.KEY_SPACE.isPressed()) pause = !pause;
                        if (!pause) {
                            ballPosition.x += ballSpeed.x
                            ballPosition.y += ballSpeed.y

                            if (useGravity) ballSpeed.y += gravity

                            // Check walls collision for bouncing
                            if ((ballPosition.x >= (screenWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f
                            if ((ballPosition.y >= (screenHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -0.95f
                        } else {
                            framesCounter++
                        }
                    }


                    onDraw {
                        drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
                        drawText("PRESS SPACE to PAUSE BALL MOVEMENT", 10, screenHeight - 25, 20, LIGHTGRAY)
                        if (useGravity) drawText("GRAVITY: ON (Press G to disable)", 10, screenHeight - 50, 20, DARKGREEN)
                        else drawText("GRAVITY: OFF (Press G to enable)", 10, screenHeight - 50, 20, RED)

                        if (pause && ((framesCounter / 30) % 2 == 1)) drawText("PAUSED", 350, 200, 30, GRAY)
                        drawFPS(10, 10)
                    }
                }
            }
        }
    }
}
