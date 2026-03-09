package me.raylib.sample.shape

import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.GREEN
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.await
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.rememberSuspendingTask
import io.github.andannn.raylib.core.setValue
import io.github.andannn.easings.Ease
import io.github.andannn.easings.awaitEasingAnimation
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

                await { KeyboardKey.KEY_ENTER.isPressed() }

                ballPositionX = -100
                ballRadius = 20
                ballAlpha = 0.0f
                state = 0
            }
        }

        onDraw {
            if (state >= 2) drawRectangle(0, 0, screenWidth, screenHeight, GREEN)
            drawCircle(ballPositionX, 200, ballRadius.toFloat(), Fade(RED, 1.0f - ballAlpha))
            if (state == 3) drawText("PRESS [ENTER] TO PLAY AGAIN!", 240, 200, 20, BLACK)
        }
    }
}
