package me.raylib.sample.shape

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import io.github.andannn.raylib.base.Colors.DARKGREEN
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.rememberSuspendingTask
import io.github.andannn.raylib.core.setValue
import io.github.andannn.easings.awaitDuration
import kotlin.time.Duration.Companion.seconds

fun ComponentRegistry.bouncingBall() {
    var pause by remember {
        mutableStateOf(initialValue = false)
    }

    component("key") {
        val ballPosition by remember {
            val nativeStateOf = nativeStateOf { alloc<Vector2> { x = screenWidth.div(2f); y = screenHeight.div(2f) } }
            nativeStateOf
        }
        val ballSpeed by remember {
            nativeStateOf { alloc<Vector2> { x = 5f; y = 4f } }
        }
        var useGravity by remember {
            mutableStateOf(false)
        }

        val ballRadius = 20
        val gravity = 0.2f

        update {
            if (KeyboardKey.KEY_G.isPressed()) useGravity = !useGravity;
            if (KeyboardKey.KEY_SPACE.isPressed()) { pause = !pause }
            if (!pause) {
                ballPosition.x += ballSpeed.x
                ballPosition.y += ballSpeed.y

                if (useGravity) ballSpeed.y += gravity

                // Check walls collision for bouncing
                if ((ballPosition.x >= (screenWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f
                if ((ballPosition.y >= (screenHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -0.95f
            }
        }

        draw {
            drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
            drawText("PRESS SPACE to PAUSE BALL MOVEMENT", 10, screenHeight - 25, 20, LIGHTGRAY)
            if (useGravity) drawText("GRAVITY: ON (Press G to disable)", 10, screenHeight - 50, 20, DARKGREEN)
            else drawText("GRAVITY: OFF (Press G to enable)", 10, screenHeight - 50, 20, RED)

            drawFPS(10, 10)
        }
    }

    if (pause) {
        component("PAUSE anime") {
            var showPaused by remember {
                mutableStateOf(true)
            }
            rememberSuspendingTask {
                while (true) {
                    awaitDuration(0.5.seconds)
                    showPaused = false
                    awaitDuration(0.5.seconds)
                    showPaused = true
                }
            }
            draw {
                if (showPaused) drawText("PAUSED", 350, 200, 30, GRAY)
            }
        }
    }
}
