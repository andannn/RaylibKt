package me.raylib.sample.shape

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors.DARKGREEN
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.MAROON
import raylib.core.Colors.RED
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.component
import raylib.core.getValue
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.remember
import raylib.core.setValue
import raylib.core.suspendingTask
import raylib.easings.awaitDuration
import kotlin.time.Duration.Companion.seconds

fun ComponentRegistry.bouncingBall() {
    var pause by remember {
        mutableStateOf(initialValue = false, triggerRebuild = true)
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

        onUpdate {
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

        onDraw {
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
            suspendingTask {
                while (true) {
                    awaitDuration(0.5.seconds)
                    showPaused = false
                    awaitDuration(0.5.seconds)
                    showPaused = true
                }
            }
            onDraw {
                if (showPaused) drawText("PAUSED", 350, 200, 30, GRAY)
            }
        }
    }
}
