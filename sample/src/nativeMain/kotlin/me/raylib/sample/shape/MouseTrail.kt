package me.raylib.sample.shape

import kotlinx.cinterop.CValue
import kotlinx.cinterop.zeroValue
import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.SKYBLUE
import raylib.core.Colors.WHITE
import raylib.core.Vector2
import raylib.core.forEachContentsIndexed
import raylib.core.window
import raylib.interop.Fade

const val MAX_TRAIL_LENGTH = 30

fun mouseTrail() {
    window(
        title = "raylib [shapes] example - mouse trail",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.BLACK
    ) {
        componentRegistry {
            val trailPositions = List<CValue<Vector2>>(MAX_TRAIL_LENGTH) { zeroValue() }.toMutableList()

            component("AA") {
                provideHandlers {
                    onUpdate {
                        trailPositions.removeLast()
                        trailPositions.add(0, mousePosition)
                    }
                    onDraw {
                        trailPositions.forEachContentsIndexed { i, position ->
                            if (position.x != 0f || position.y != 0f) {
                                val ratio = (MAX_TRAIL_LENGTH - i).toFloat() / MAX_TRAIL_LENGTH
                                val trailColor = Fade(SKYBLUE, ratio * 0.5f + 0.5f)
                                val trailRadius = 15.0f * ratio
                                drawCircle(trailPositions[i], trailRadius, trailColor)
                            }
                        }

                        drawCircle(trailPositions[0], 15.0f, WHITE)
                        drawText("Move the mouse to see the trail effect!", 10, screenHeight - 30, 20, LIGHTGRAY)
                    }
                }
            }
        }
    }
}