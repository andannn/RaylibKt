package me.raylib.sample.core

import kotlinx.cinterop.copy
import io.github.andannn.raylib.foundation.Colors
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.raylib.foundation.windowContext
import raylib.interop.KeyboardKey

private const val speed = 10f
private const val circleRadius = 32f

internal fun ComponentRegistry.deltaTime() {
    component("key") {
        var deltaCircle by remember {
            mutableStateOf(Vector2(y = screenHeight.div(3f)))
        }
        var frameCircle by remember {
            mutableStateOf(Vector2(y = screenHeight.div(3f).times(2f)))
        }

        update {
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
        draw {
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

            drawText("FPS: ${windowContext.currentFps}", 10, 10, 20, Colors.DARKGRAY)
            drawText(
                "Frame time: ${windowContext.frameTimeSeconds.times(1000)} ms",
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
