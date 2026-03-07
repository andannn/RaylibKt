package me.raylib.sample.shape

import kotlinx.cinterop.CValue
import kotlinx.cinterop.zeroValue
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.SKYBLUE
import raylib.core.Colors.WHITE
import raylib.core.ComponentRegistry
import raylib.core.Vector2
import raylib.core.component
import raylib.core.forEachContentsIndexed
import raylib.core.remember
import raylib.interop.Fade

const val MAX_TRAIL_LENGTH = 30

fun ComponentRegistry.mouseTrail() {
    val trailPositions = remember {
        List<CValue<Vector2>>(MAX_TRAIL_LENGTH) { zeroValue() }.toMutableList()
    }

    component("AA") {
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
