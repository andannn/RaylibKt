package me.raylib.sample.shape

import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.GREEN
import raylib.core.Colors.RED
import raylib.core.KeyboardKey
import raylib.core.await
import raylib.core.window
import raylib.easings.Ease
import raylib.easings.awaitEasingAnimation
import raylib.interop.Fade
import kotlin.time.Duration.Companion.seconds

fun easingBall() {
    window(
        title = "raylib [shapes] example - easings ball",
        width = 800,
        height = 450,
        initialFps = 60,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("A") {
                var ballPositionX = -100
                var ballRadius = 20
                var ballAlpha = 0.0f

                var state = 0

                provideHandlers {
                    suspendingScope {
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
        }
    }
}
