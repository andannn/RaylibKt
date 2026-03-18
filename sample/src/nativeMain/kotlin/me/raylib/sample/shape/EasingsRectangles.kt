package me.raylib.sample.shape

import io.github.andannn.easings.Ease
import io.github.andannn.easings.animateTo
import io.github.andannn.easings.awaitDuration
import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.RectangleAlloc
import io.github.andannn.raylib.core.await
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.rememberSuspendingTask
import io.github.andannn.raylib.core.setValue
import kotlinx.cinterop.readValue
import raylib.interop.Rectangle
import kotlin.time.Duration.Companion.seconds

private const val RECS_HEIGHT = 50
private const val RECS_WIDTH = 50

fun ComponentRegistry.easingsRectangles() {
    component("AA") {
        val maxRecsX = screenWidth.div(RECS_WIDTH)
        val maxRecsY = screenHeight.div(RECS_HEIGHT)
        val recs: List<Rectangle> = remember {
            val list = mutableListOf<Rectangle>()

            for (y in 0 until maxRecsY) {
                for (x in 0 until maxRecsX) {
                    list.add(
                        RectangleAlloc(
                            x = RECS_WIDTH / 2f + RECS_WIDTH * x,
                            y = RECS_HEIGHT / 2f + RECS_HEIGHT * y,
                            width = RECS_WIDTH.toFloat(),
                            height = RECS_HEIGHT.toFloat()
                        ).value
                    )
                }
            }

            list
        }
        var rotation by remember {
            mutableStateOf(0f)
        }
        var isWaitingKey by remember {
            mutableStateOf(false)
        }

        rememberSuspendingTask {
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
        }

        draw {
            recs.forEach { rec ->
                drawRectangle(
                    rectangle = rec.readValue(),
                    origin = Vector2(rec.width / 2f, rec.height / 2f),
                    rotation = rotation,
                    color = RED
                )
            }
            if (isWaitingKey) {
                drawText("PRESS [SPACE] TO PLAY AGAIN!", 240, 200, 20, GRAY)
            }
        }
    }
}
