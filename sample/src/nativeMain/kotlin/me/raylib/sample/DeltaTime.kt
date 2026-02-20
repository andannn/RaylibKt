package me.raylib.sample

import kotlinx.cinterop.copy
import raylib.core.Colors
import raylib.core.RlMouse
import raylib.core.Vector2
import raylib.core.drawScope
import raylib.core.mainGameLoop
import raylib.core.textDrawScope
import raylib.core.window
import raylib.interop.KeyboardKey

internal fun deltaTime() {
    window(
        title = "raylib [core] example - delta time",
        initialFps = 30,
        width = 800,
        height = 450
    ) {
        var deltaCircle = Vector2(y = windowHeight.div(3f))
        var frameCircle = Vector2(y = windowHeight.div(3f).times(2f))
        val speed = 10f
        val circleRadius = 32f

        mainGameLoop {
            currentFps += RlMouse.wheelMove().toInt()

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
            drawScope {
                clearBackground(Colors.RAYWHITE)
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

                textDrawScope {
                    draw("FPS: $currentFps", 10, 10, 20, Colors.DARKGRAY)
                    draw("Frame time: ${frameTimeSeconds.times(1000)} ms", 10, 30, 20, Colors.DARKGRAY)
                    draw("FUNC: x += GetFrameTime()*speed", 10, 90, 20, Colors.RED);
                    draw("FUNC: x += speed", 10, 240, 20, Colors.BLUE)
                }
            }
        }
    }
}