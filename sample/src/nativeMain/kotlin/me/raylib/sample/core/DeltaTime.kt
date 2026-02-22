package me.raylib.sample.core

import kotlinx.cinterop.copy
import raylib.core.Colors
import raylib.core.Vector2
import raylib.core.window
import raylib.interop.KeyboardKey

internal fun deltaTime() {
    window(
        title = "raylib [core] example - delta time",
        initialFps = 2,
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        gameComponent {
            var deltaCircle = Vector2(y = screenHeight.div(3f))
            var frameCircle = Vector2(y = screenHeight.div(3f).times(2f))
            val speed = 10f
            val circleRadius = 32f
            provideHandlers {

            }
            provideHandlers {
                onUpdate {
                    currentFps += mouseWheelMove.toInt()

                    deltaCircle = deltaCircle.copy {
                        x += frameTimeSeconds * 6.0f * speed
                    }
                    frameCircle = frameCircle.copy {
                        x += 0.1f * speed
                    }

                    if (KeyboardKey.KEY_R.isPressed()) {
                        deltaCircle = deltaCircle.copy { x = 0f }
                        frameCircle = frameCircle.copy { x = 0f }
                    }
                }
                onDraw {
                    drawCircle(
                        center = deltaCircle,
                        radius = circleRadius,
                        color = Colors.RED
                    )
                    drawCircle(
                        center = frameCircle,
                        radius = circleRadius,
                        color = Colors.BLUE
                    )

                    drawText("FPS: $currentFps", 10, 10, 20, Colors.DARKGRAY)
                    drawText(
                        "Frame time: ${frameTimeSeconds.times(1000)} ms",
                        10,
                        30,
                        20,
                        Colors.DARKGRAY
                    )
                    drawText("FUNC: x += GetFrameTime()*speed", 10, 90, 20, Colors.RED);
                    drawText("FUNC: x += speed", 10, 240, 20, Colors.BLUE)
                }
            }
        }
    }
}