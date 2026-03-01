package me.raylib.sample.shape

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.GRAY
import raylib.core.Colors.RED
import raylib.core.KeyboardKey
import raylib.core.RectangleAlloc
import raylib.core.Vector2Alloc
import raylib.core.await
import raylib.core.stateOf
import raylib.core.window
import raylib.easings.Ease
import raylib.easings.animateTo
import raylib.easings.awaitDuration
import raylib.interop.Rectangle
import kotlin.time.Duration.Companion.seconds

private const val RECS_HEIGHT = 50
private const val RECS_WIDTH = 50

fun easingsRectangles() {
    window(
        title = "raylib [shapes] example - easings box",
        width = 800,
        height = 450,
        initialFps = 60,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            val maxRecsX = screenWidth.div(RECS_WIDTH)
            val maxRecsY = screenHeight.div(RECS_HEIGHT)
            val recs: List<Rectangle> by stateOf {
                val list = mutableListOf<Rectangle>()

                for (y in 0 until maxRecsY) {
                    for (x in 0 until maxRecsX) {
                        list.add(
                            RectangleAlloc(
                                x = RECS_WIDTH / 2f + RECS_WIDTH * x,
                                y = RECS_HEIGHT / 2f + RECS_HEIGHT * y,
                                width = RECS_WIDTH.toFloat(),
                                height = RECS_HEIGHT.toFloat()
                            )
                        )
                    }
                }

                list
            }
            var rotation = 0f
            var isWaitingKey = false
            component("AA") {
                provideHandlers {
                    suspendingTask {
                        while (true) {
                            awaitDuration(4.seconds) { fraction ->
                                recs.forEach { rec ->
                                    rec.height = RECS_HEIGHT.toFloat().animateTo(0f, fraction, Ease.CircOut)
                                    rec.width = RECS_WIDTH.toFloat().animateTo(0f, fraction, Ease.CircOut)
                                    rotation = 0f.animateTo(360f, fraction, Ease.LinearIn)
                                }
                            }

                            isWaitingKey = true
                            await { KeyboardKey.KEY_SPACE.isPressed() }
                            isWaitingKey = false

                            recs.forEach { rec ->
                                rec.height = RECS_HEIGHT.toFloat()
                                rec.width = RECS_WIDTH.toFloat()
                                rotation = 0f
                            }
                        }
                    }.start()

                    onDraw {
                        recs.forEach { rec ->
                            memScoped {
                                drawRectangle(
                                    rectangle = rec.readValue(),
                                    origin = Vector2Alloc(rec.width / 2f, rec.height / 2f).readValue(),
                                    rotation = rotation,
                                    color = RED
                                )
                            }
                        }
                        if (isWaitingKey) {
                            drawText("PRESS [SPACE] TO PLAY AGAIN!", 240, 200, 20, GRAY)
                        }
                    }
                }
            }
        }
    }
}