package me.raylib.sample.shape

import kotlinx.cinterop.readValue
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.BLUE
import io.github.andannn.raylib.base.Colors.GOLD
import io.github.andannn.raylib.base.Colors.LIME
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.base.RectangleAlloc
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.base.getCollisionRec
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.base.isCollisionWith
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.base.set
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue
import raylib.interop.MeasureText

private const val screenUpperLimit = 40

fun ComponentRegistry.collisionArea() {
    component("K") {
        val boxA by remember {
            nativeStateOf { RectangleAlloc(10f, screenHeight / 2.0f - 50, 200f, 100f) }
        }
        var boxASpeedX by remember {
            mutableStateOf(4)
        }
        val boxB by remember {
            nativeStateOf { RectangleAlloc(screenWidth / 2.0f - 30, screenHeight / 2.0f - 30, 60f, 60f) }
        }
        val boxCollision by remember {
            nativeStateOf { RectangleAlloc() }
        }
        var pause by remember {
            mutableStateOf(false)
        }
        var collision by remember {
            mutableStateOf(false)
        }

        onUpdate {
            if (!pause) boxA.x += boxASpeedX
            if (((boxA.x + boxA.width) >= screenWidth) || (boxA.x <= 0)) boxASpeedX *= -1
            boxB.x = mouseX - boxB.width / 2;
            boxB.y = mouseY - boxB.height / 2;

            if ((boxB.x + boxB.width) >= screenWidth) {
                boxB.x = screenWidth - boxB.width;
            } else if (boxB.x <= 0) {
                boxB.x = 0f
            }

            if ((boxB.y + boxB.height) >= screenHeight) {
                boxB.y = screenHeight - boxB.height
            } else if (boxB.y <= screenUpperLimit) {
                boxB.y = screenUpperLimit.toFloat()
            }

            collision = boxA.readValue().isCollisionWith(boxB.readValue())
            if (collision) {
                boxCollision.set(boxA.readValue().getCollisionRec(boxB.readValue()))
            }
            if (KeyboardKey.KEY_SPACE.isPressed()) pause = !pause;
        }

        onDraw {
            drawRectangle(0, 0, screenWidth, screenUpperLimit, if (collision) RED else BLACK)
            drawRectangle(boxA.readValue(), GOLD)
            drawRectangle(boxB.readValue(), BLUE)
            if (collision) {
                // Draw collision area
                drawRectangle(boxCollision.readValue(), LIME)

                // Draw collision message
                drawText(
                    "COLLISION!",
                    screenWidth / 2 - MeasureText("COLLISION!", 20) / 2,
                    screenUpperLimit / 2 - 10,
                    20,
                    BLACK
                )

                // Draw collision area
                drawText(
                    "Collision Area: ${boxCollision.width * boxCollision.height}",
                    screenWidth / 2 - 100,
                    screenUpperLimit + 10,
                    20,
                    BLACK
                )
                drawFPS(10, 10);
            }
        }
    }
}
