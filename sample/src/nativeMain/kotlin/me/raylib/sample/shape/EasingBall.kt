package me.raylib.sample.shape

import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.GREEN
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.rememberSuspendingTask
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.easings.Ease
import io.github.andannn.easings.awaitEasingAnimation
import io.github.andannn.raylib.foundation.GameContext
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.await
import io.github.andannn.raylib.runtime.find
import raylib.interop.Fade
import kotlin.time.Duration.Companion.seconds

fun ComponentRegistry.easingBall() {
    component("A") {
        var ballPositionX by remember {
            mutableStateOf(-100)
        }
        var ballRadius by remember {
            mutableStateOf(20)
        }
        var ballAlpha by remember {
            mutableStateOf(0.0f)
        }

        var state by remember {
            mutableStateOf(0)
        }

        val gameContext = remember {
            find<GameContext>()
        }
        rememberSuspendingTask {
            while (true) {
                awaitEasingAnimation(
                    start = ballPositionX.toFloat(),
                    target = screenWidth.div(2f),
                    duration = 2.seconds,
                    easing = Ease.ElasticInOut,
                    onUpdate = { ballPositionX = it.toInt() }
                )
                state = 1
                awaitEasingAnimation(
                    start = ballRadius.toFloat(),
                    target = 500f,
                    duration = 3.seconds,
                    easing = Ease.ElasticIn,
                    onUpdate = { ballRadius = it.toInt() }
                )
                state = 2
                awaitEasingAnimation(
                    start = ballAlpha,
                    target = 1f,
                    duration = 3.seconds,
                    easing = Ease.CubicOut,
                    onUpdate = { ballAlpha = it }
                )
                state = 3
                await {
                    with(gameContext) {
                        KeyboardKey.KEY_ENTER.isPressed()
                    }
                }

                ballPositionX = -100
                ballRadius = 20
                ballAlpha = 0.0f
                state = 0
            }
        }

        draw {
            if (state >= 2) drawRectangle(0, 0, screenWidth, screenHeight, GREEN)
            drawCircle(ballPositionX, 200, ballRadius.toFloat(), Fade(RED, 1.0f - ballAlpha))
            if (state == 3) drawText("PRESS [ENTER] TO PLAY AGAIN!", 240, 200, 20, BLACK)
        }
    }
}
